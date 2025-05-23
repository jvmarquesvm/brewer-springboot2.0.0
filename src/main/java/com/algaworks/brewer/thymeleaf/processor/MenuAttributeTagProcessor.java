package com.algaworks.brewer.thymeleaf.processor;

import javax.servlet.http.HttpServletRequest;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring5.util.FieldUtils;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

public class MenuAttributeTagProcessor  extends AbstractAttributeTagProcessor {
	
	private static final String NOME_ATRIBUTO = "menu";
	private static final int PRECEDENCIA = 1000;

	public MenuAttributeTagProcessor( String dialectPrefix ) {
		super(TemplateMode.HTML, dialectPrefix, null, false, NOME_ATRIBUTO, true, PRECEDENCIA, true);
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
			String attributeValue, IElementTagStructureHandler structureHandler) {
		
		//boolean hasErrors = FieldUtils.hasErrors(context, attributeValue);
		HttpServletRequest request = ((IWebContext) context).getRequest();
		String uri = request.getRequestURI();
		
		IEngineConfiguration configuration = context.getConfiguration();
		IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
		IStandardExpression expression = parser.parseExpression(context, attributeValue);
		String menu = (String) expression.execute(context);
		
		System.out.println("Menu: " + menu + " uri:" + uri);
		
		//modificanado startsWith para match para fazer funcionar o menu
		//if (uri.startsWith(menu)) {
		if (uri.matches(menu)) {
			String classesExistentes = tag.getAttributeValue("class");
			structureHandler.setAttribute("class", classesExistentes + " is-active");
		}
		
	}
	
}
