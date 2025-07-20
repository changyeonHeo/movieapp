package com.example.demo;

//RestController를 위한 import
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class MovieRestController {

 private final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1MzZjMmUxYzQ4YmIyOTg4OTY3NDhlZGY0NWEwMjA5MSIsIm5iZiI6MTc1MjkxNDkzMS4yMjcsInN1YiI6IjY4N2I1YmYzMmM1YWEzMTM0NTM4ZDBiZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IjPt17wuU226wy-Qd0CmQuO-UZ0LTsHCNas9eCYTwDA";

 @GetMapping("/movie/{id}")
 public ResponseEntity<?> getMovieDetail(@PathVariable("id") String movieId) {
     try {
         String detailUrl = "https://api.themoviedb.org/3/movie/" + movieId + "?language=ko-KR";
         JSONObject movie = fetchMovieDetail(detailUrl);
         return ResponseEntity.ok(movie.toMap());
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("영화 정보 로딩 실패: " + e.getMessage());
     }
 }

 // 공용으로 사용될 fetch 함수 복사
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

