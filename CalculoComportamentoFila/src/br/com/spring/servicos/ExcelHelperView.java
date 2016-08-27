package br.com.spring.servicos;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import br.com.spring.dominio.Parametro;

public class ExcelHelperView extends AbstractExcelView{
	
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workbook,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		HSSFSheet excelSheet = workbook.createSheet("Distribuicao");

		setExcelHeader(excelSheet);
		
		@SuppressWarnings("unchecked")
		List<Parametro> parametros = (List<Parametro>) model.get("parametros");
		setExcelRows(excelSheet,parametros);
		
	}

	public void setExcelHeader(HSSFSheet excelSheet) {
		HSSFRow excelHeader = excelSheet.createRow(0);
		excelHeader.createCell(0).setCellValue("K");
		excelHeader.createCell(1).setCellValue("Probabilidade Acumulada");

	}
	
	public void setExcelRows(HSSFSheet excelSheet, List<Parametro> parametroList){
		int record = 1;
		for (Parametro parametro : parametroList) {
			HSSFRow excelRow = excelSheet.createRow(record++);
			excelRow.createCell(0).setCellValue(parametro.getValorTempo());
			excelRow.createCell(1).setCellValue(parametro.getValorParametro());
	
		}
	}
}
