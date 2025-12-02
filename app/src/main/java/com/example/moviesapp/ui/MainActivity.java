package com.example.moviesapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.R;
import com.example.moviesapp.adapter.MovieAdapter;
import com.example.moviesapp.model.Movie;
import com.example.moviesapp.model.MovieResponse;
import com.example.moviesapp.network.ApiClient;
import com.example.moviesapp.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMovies;
    private MovieAdapter movieAdapter;
    private Spinner spinnerFilter;
    private EditText etSearch;
    private Button btnSearch;

    // FILTRO ACTUAL
    private String currentFilter = "Popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMovies = findViewById(R.id.rvMovies);
        spinnerFilter = findViewById(R.id.spinnerFilter);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);

        // Configurar RecyclerView
        movieAdapter = new MovieAdapter(this);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(movieAdapter);

        // Cargar películas del filtro elegido
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentFilter = parent.getItemAtPosition(position).toString();
                loadMoviesByFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Botón buscar
        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim();
            if (query.isEmpty()) {
                loadMoviesByFilter();
            } else {
                searchMovies(query);
            }
        });

        // Cargar películas apenas inicia la app
        loadMoviesByFilter();
    }

    //   Cargar películas según filtro
    private void loadMoviesByFilter() {
        ApiService apiService = ApiClient.getApiService();
        Call<MovieResponse> call;

        String language = "es-ES";

        switch (currentFilter) {
            case "Top Rated":
                call = apiService.getTopRatedMovies(ApiClient.API_KEY, language, 1);
                break;

            case "Upcoming":
                call = apiService.getUpcomingMovies(ApiClient.API_KEY, language, 1);
                break;

            case "Popular":
            default:
                call = apiService.getPopularMovies(ApiClient.API_KEY, language, 1);
                break;
        }

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();
                    movieAdapter.setMovieList(movies);
                } else {
                    Toast.makeText(MainActivity.this, "Error al cargar películas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //   Buscar películas
    private void searchMovies(String query) {
        ApiService apiService = ApiClient.getApiService();
        Call<MovieResponse> call =
                apiService.searchMovies(ApiClient.API_KEY, "es-ES", query, 1);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();
                    movieAdapter.setMovieList(movies);
                } else {
                    Toast.makeText(MainActivity.this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error al buscar: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
