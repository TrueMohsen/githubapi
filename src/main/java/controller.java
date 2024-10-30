import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class controller {
    private static final String GITHUB_API_URL = "https://api.github.com/repos/";

    public static void main(String[] args) {
        String repoOwner = "angular"; // Replace with the repository owner's name
        String repoName = "angular"; // Replace with the repository name

        String url = GITHUB_API_URL + repoOwner + "/" + repoName;
        String urlForPulls = GITHUB_API_URL + repoOwner + "/" + repoName + "/pulls";

        try {
            // Create an HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Create a GET request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Accept", "application/vnd.github.v3+json") // Use the GitHub API v3 format
                    .build();
            HttpRequest requestForPulls = HttpRequest.newBuilder()
                    .uri(new URI(urlForPulls))
                    .build();


            // Send the GET request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> responseForPulls = client.send(requestForPulls, HttpResponse.BodyHandlers.ofString());

            // Check if the request was successful
            if (response.statusCode() == 200 && responseForPulls.statusCode() == 200) {
                // Parse the JSON response
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.body());
                JsonNode jsonNodeForPull = objectMapper.readTree(responseForPulls.body());

                // Extracting information
                int forksCount = jsonNode.path("forks_count").asInt();
                int stargazersCount = jsonNode.path("stargazers_count").asInt();
                int openIssuesCount = jsonNode.path("open_issues_count").asInt();


                System.out.println("Forks: " + forksCount);
                System.out.println("Stars: " + stargazersCount);
                System.out.println("Tags: " + openIssuesCount);
//                System.out.println("jsonNode: " + jsonNode);
                System.out.println("responseForPulls: " + jsonNodeForPull);
            } else {
                System.out.println("Error: Unable to fetch repository information. HTTP Status Code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately in real applications
        }
    }
}
