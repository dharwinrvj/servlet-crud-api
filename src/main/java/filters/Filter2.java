package filters;

import javax.servlet.*;
import java.io.IOException;

public class Filter2 implements Filter {
    public void init(FilterConfig arg) {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("before filter2");
        chain.doFilter(request, response);
        System.out.println("after filter2");
    }

    public void destroy() {
    }
}
