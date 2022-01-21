package br.com.projetousuarios.controller;

import java.io.IOException;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(value={"/admin/*"}, dispatcherTypes={DispatcherType.REQUEST, DispatcherType.FORWARD})
public class AutenticacaoFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
		//obtem a sessão
        HttpSession session = httpRequest.getSession(false);
        boolean isLogado = (session != null && session.getAttribute("usuario") != null);
        
        //verifica se a sessão existe e se o usuário não está logado
		if ( !isLogado ) {  
			//usuário não logado, envie para página de login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/index.jsp");
        } else { // usuário logado
            // permite a requisição continuar        	
            chain.doFilter(request, response);
        }

	}

}
