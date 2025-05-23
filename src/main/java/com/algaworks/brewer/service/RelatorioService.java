package com.algaworks.brewer.service;

import java.io.InputStream;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import com.algaworks.brewer.dto.PeriodoRelatorio;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class RelatorioService {
	@Autowired
	private DataSource datasource;
	
	public byte[] gerarRelatorioVendasEmitidas(PeriodoRelatorio periodoRelatorio) throws Exception {
		Date dataInicio = Date.from(LocalDateTime.of(periodoRelatorio.getDataInicio(), LocalTime.of(0, 0, 0)).atZone(ZoneId.systemDefault()).toInstant());
		Date dataFim = Date.from(LocalDateTime.of(periodoRelatorio.getDataFim(), LocalTime.of(23, 59, 59)).atZone(ZoneId.systemDefault()).toInstant());
		
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("format", "pdf");
		parametros.put("data_inicio", dataInicio);
		parametros.put("data_fim", dataFim);
		
		InputStream jasperStream = this.getClass().getResourceAsStream("/relatorios/relatorio_vendas_emitidas.jasper");
		Connection conection = this.datasource.getConnection();
		//Utilizando a api do jasper diretamente / sem configuração prévia
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, parametros, conection);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} finally {
			conection.close();
		}
	}
}
