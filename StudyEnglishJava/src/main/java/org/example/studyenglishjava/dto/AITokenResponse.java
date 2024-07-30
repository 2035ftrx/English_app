package org.example.studyenglishjava.dto;

public record AITokenResponse(

        String refresh_token,
        String expires_in,
        String session_key,
        String access_token,
        String scope,
        String session_secret

) {

}