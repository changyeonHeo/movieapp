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
import java.util.*;

@Controller
public class MovieController {

    private final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1MzZjMmUxYzQ4YmIyOTg4OTY3NDhlZGY0NWEwMjA5MSIsIm5iZiI6MTc1MjkxNDkzMS4yMjcsInN1YiI6IjY4N2I1YmYzMmM1YWEzMTM0NTM4ZDBiZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IjPt17wuU226wy-Qd0CmQuO-UZ0LTsHCNas9eCYTwDA";

    // 메인 - 인기 영화
    @GetMapping("/")
    public String index(Model model) {
        try {
            String apiUrl = "https://api.themoviedb.org/3/movie/popular?language=ko-KR&page=1";
            List<Map<String, Object>> movieList = fetchMovies(apiUrl);
            model.addAttribute("movies", movieList);
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

            List<Map<String, Object>> movies = fetchMovies(apiUrl); // ← List<Map>으로 바꿈
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
            // 1. 영화 상세 정보
            String detailUrl = "https://api.themoviedb.org/3/movie/" + movieId + "?language=ko-KR";
            JSONObject movie = fetchMovieDetail(detailUrl);
            model.addAttribute("movie", movie.toMap());

            // 2. 출연진/감독 정보
            String creditUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?language=ko-KR";
            JSONObject credit = fetchMovieDetail(creditUrl);

            JSONArray castArray = credit.getJSONArray("cast");
            JSONArray crewArray = credit.getJSONArray("crew");

            // 2-1. 배우 리스트 (최대 10명만)
            List<Map<String, Object>> castList = new ArrayList<>();
            for (int i = 0; i < Math.min(10, castArray.length()); i++) {
                JSONObject cast = castArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<>();
                map.put("name", cast.getString("name"));
                map.put("character", cast.optString("character", ""));
                map.put("profile_path", cast.optString("profile_path", null));
                castList.add(map);
            }

            // 2-2. 감독 정보
            String director = "";
            for (int i = 0; i < crewArray.length(); i++) {
                JSONObject crew = crewArray.getJSONObject(i);
                if ("Director".equals(crew.optString("job"))) {
                    director = crew.optString("name", "");
                    break;
                }
            }

            model.addAttribute("castList", castList);
            model.addAttribute("director", director);

        } catch (Exception e) {
            model.addAttribute("error", "상세 정보 실패: " + e.getMessage());
        }
        return "detail";
    }


    // 영화 목록 가져오기 - JSONArray → List<Map>
    private List<Map<String, Object>> fetchMovies(String apiUrl) throws Exception {
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
        JSONArray results = json.getJSONArray("results");

        List<Map<String, Object>> movieList = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject movieObj = results.getJSONObject(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", movieObj.getInt("id"));
            map.put("title", movieObj.optString("title", ""));
            map.put("poster_path", movieObj.optString("poster_path", null));
            map.put("release_date", movieObj.optString("release_date", ""));
            map.put("overview", movieObj.optString("overview", ""));
            map.put("vote_average", movieObj.optDouble("vote_average", 0));
            movieList.add(map);
        }

        return movieList;
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
