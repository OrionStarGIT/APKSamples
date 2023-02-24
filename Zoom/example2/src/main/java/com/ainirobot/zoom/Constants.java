package com.ainirobot.zoom;

public interface Constants {

	// TODO Change it to your web domain
    String WEB_DOMAIN = "zoom.us";

	// TODO change it to your user ID
    String USER_ID = "Your user ID from REST API";
	
	// TODO change it to your token
    String ZOOM_ACCESS_TOKEN = "Your zak from REST API";
	
	// TODO Change it to your exist meeting ID to start meeting
    String MEETING_ID = "85224729671";

    /**
     * We recommend that, you can generate jwttoken on your own server instead of hardcore in the code.
     * We hardcore it here, just to run the demo.
     *
     * You can generate a jwttoken on the https://jwt.io/
     * with this payload:
     * {
     *     "appKey": "string", // app key
     *     "iat": long, // access token issue timestamp
     *     "exp": long, // access token expire time
     *     "tokenExp": long // token expire time
     * }
     */
    public final static String SDK_JWTTOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzZGtLZXkiOiJCNjMwVW1GenBRTkV6aWZncFhFQ0d4SjdneEowTHZkelo0bVUiLCJpYXQiOjE2NzE2MDIxOTUsImV4cCI6MTY3MTYwOTM5NSwiYXBwS2V5IjoiQjYzMFVtRnpwUU5FemlmZ3BYRUNHeEo3Z3hKMEx2ZHpaNG1VIiwidG9rZW5FeHAiOjE2NzE2MDkzOTV9.ePUhT4AyZdi5vhr-MjreuRjGj6uW9YbqkBLyY-gQG6s";

}
