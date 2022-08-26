package filters;

import javax.servlet.*;
import java.io.IOException;

public class Filter1 implements Filter {
    public void init(FilterConfig arg) {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("before filter1");
        chain.doFilter(request, response);
        System.out.println("after filter1");
    }

    public void destroy() {
    }
}
