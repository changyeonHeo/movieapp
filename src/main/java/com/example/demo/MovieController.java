package com.example.demo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Controller
public class MovieController {

    private final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1MzZjMmUxYzQ4YmIyOTg4OTY3NDhlZGY0NWEwMjA5MSIsIm5iZiI6MTc1MjkxNDkzMS4yMjcsInN1YiI6IjY4N2I1YmYzMmM1YWEzMTM0NTM4ZDBiZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IjPt17wuU226wy-Qd0CmQuO-UZ0LTsHCNas9eCYTwDA";

    // 메인 - 인기 영화
    @GetMapping("/")
    public String index(Model model) {
        try {
            String apiUrl = "https://api.themoviedb.org/3/movie/popular?language=ko-KR&page=1";
            JSONArray movies = fetchMovies(apiUrl);
            model.addAttribute("movies", movies);
        } catch (Exception e) {
            model.addAttribute("error", "기본 영화 로딩 실패: " + e.getMessage());
        }
        return "index";
    }

    // 검색
    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        try {
            String encoded = URLEncoder.encode(query, "UTF-8");
            String apiUrl = "https://api.themoviedb.org/3/search/movie?language=ko-KR&query=" + encoded;
            JSONArray movies = fetchMovies(apiUrl);
            model.addAttribute("movies", movies);
        } catch (Exception e) {
            model.addAttribute("error", "검색 실패: " + e.getMessage());
        }
        return "result";
    }

    // 상세 정보
    @GetMapping("/movie")
    public String detail(@RequestParam("id") String movieId, Model model) {
        try {
            String apiUrl = "https://api.themoviedb.org/3/movie/" + movieId + "?language=ko-KR";
            JSONObject movie = fetchMovieDetail(apiUrl);
            model.addAttribute("movie", movie);
        } catch (Exception e) {
            model.addAttribute("error", "상세 정보 실패: " + e.getMessage());
        }
        return "detail";
    }

    // 영화 목록 가져오기
    private JSONArray fetchMovies(String apiUrl) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");

        conn.setRequestProperty("Authorization", "Bearer " + BEARER_TOKEN);
        conn.setRequestProperty("accept", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();

        JSONObject json = new JSONObject(sb.toString());
        return json.getJSONArray("results");
    }


    private JSONObject fetchMovieDetail(String apiUrl) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");


        conn.setRequestProperty("Authorization", "Bearer " + BEARER_TOKEN);
        conn.setRequestProperty("accept", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();

        return new JSONObject(sb.toString());
    }
}
