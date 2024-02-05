package guru.sfg.watery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class RestHeaderAuthFilter extends AbstractRestAuthFilter {

    /**
     * @param requiresAuthenticationRequestMatcher
     */
    public RestHeaderAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    protected String getPassword(HttpServletRequest request) {
        return request.getHeader("Api-Secret");
        //return request.getParameter("apiSecret");
    }

    protected String getUsername(HttpServletRequest request) {
          return request.getHeader("Api-Key");
          //return request.getParameter("apiKey");
    }


}
