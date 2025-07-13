package com.example.demo;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
public class MovieController {

    private final String API_KEY = "31707be";

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/search")
    public String search(String query, Model model) {
        try {
            String encoded = java.net.URLEncoder.encode(query, "UTF-8");
            String apiUrl = "https://www.omdbapi.com/?apikey=" + API_KEY + "&s=" + encoded;

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();

            JSONObject json = new JSONObject(sb.toString());
            model.addAttribute("result", json);

        } catch (Exception e) {
            model.addAttribute("error", "검색 중 오류 발생: " + e.getMessage());
        }

        return "result";
    }
}
