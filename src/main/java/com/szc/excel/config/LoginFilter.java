//package com.szc.excel.config;
//
//import org.springframework.context.annotation.Configuration;
//
//import java.io.IOException;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
///**
// * Servlet Filter implementation class LoginFilter
// */
//@Configuration
//public class LoginFilter implements Filter {
//
//    /**
//     * Default constructor.
//     */
//    public LoginFilter() {
//        // TODO Auto-generated constructor stub
//    }
//
//	/**
//	 * @see Filter#destroy()
//	 */
//	public void destroy() {
//		// TODO Auto-generated method stub
//	}
//
//	/**
//	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
//	 */
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		// TODO Auto-generated method stub
//		// place your code here
//		HttpServletRequest req = (HttpServletRequest)request;
//		HttpServletResponse res = (HttpServletResponse)response;
//		HttpSession session = (HttpSession)req.getSession();
//		String path = req.getRequestURL().toString();
//		if(!path.endsWith("login.html")){
//			if(!"login".equals(session.getAttribute("login"))){
//				res.sendRedirect(req.getContextPath()+"/login.html");
//			}
//		}
//		chain.doFilter(request, response);
//	}
//
//	/**
//	 * @see Filter#init(FilterConfig)
//	 */
//	public void init(FilterConfig fConfig) throws ServletException{
//		// TODO Auto-generated method stub
//	}
//
//}
