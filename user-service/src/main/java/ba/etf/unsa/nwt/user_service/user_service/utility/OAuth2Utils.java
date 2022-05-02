package ba.etf.unsa.nwt.user_service.user_service.utility;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
@Data
public class OAuth2Utils {

    @Value("${token.private-key}")
    public String privateKey;
    @Value("${token.password}")
    public String password;
    @Value("${token.alias}")
    public String alias;
}
