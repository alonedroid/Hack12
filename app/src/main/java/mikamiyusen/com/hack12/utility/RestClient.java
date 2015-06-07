package mikamiyusen.com.hack12.utility;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.androidannotations.api.rest.RestClientHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Rest(rootUrl = "", converters = {MappingJackson2HttpMessageConverter.class})
public interface RestClient extends RestClientHeaders {

    @Get("/logoff.php")
    @Accept(MediaType.APPLICATION_JSON)
    String requestMacRock();
}