package ba.etf.unsa.nwt.user_service.user_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class GrantTokenRequest {

    @NotNull(message = "Grant type can't be null")
    @Pattern(regexp = "password|refresh_token", message = "OAuth2 grant not supported")
    @JsonProperty("grant_type")
    private final String grantType;

    @JsonProperty("email")
    private final String email;

    @JsonProperty("password")
    private final String password;

    @JsonProperty("scope")
    private final String scope;

    @JsonProperty("refresh_token")
    private final String refreshToken;

    public Map<String, String> toForm() {
        final Map<String, String> form = new HashMap<>();
        form.put("grant_type", grantType);
        if (scope != null) form.put("scope", scope);
        if (grantType.equals("password")) {
            form.put("username", email);
            form.put("password", password);
        }
        else form.put("refresh_token", refreshToken);
        return form;
    }
}
