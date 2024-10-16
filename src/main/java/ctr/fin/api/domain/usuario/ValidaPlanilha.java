package ctr.fin.api.domain.usuario;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.Iterator;

public class ValidaPlanilha {
    public static boolean isExcelFileValid(MultipartFile file) {
        // Verifica se o arquivo está vazio
        if (file == null || file.isEmpty()) {
            System.out.println("O arquivo está vazio.");
            return false;
        }

        try (InputStream inputStream = file.getInputStream()) {
            // Cria uma instância da planilha Excel
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Obtém a primeira aba da planilha

            // Verifica se a planilha possui alguma linha
            Iterator<Row> rowIterator = sheet.iterator();
            if (!rowIterator.hasNext()) {
                System.out.println("A planilha está vazia.");
                return false;
            }

            if (rowIterator.hasNext()) {
                rowIterator.next(); // Pula a primeira linha (cabeçalho)
            }


            // Verifica se há pelo menos uma linha com dados (desconsiderando cabeçalhos ou linhas em branco)
            while (rowIterator.hasNext()) {


                Row row = rowIterator.next();
                if (!isRowEmpty(row)) {
                    return true; // Há uma linha válida
                }
            }

            System.out.println("Nenhuma linha gravada na planilha.");
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método auxiliar para verificar se uma linha está vazia
    private static boolean isRowEmpty(Row row) {
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false; // Se qualquer célula da linha não estiver em branco, a linha não está vazia
            }
        }
        return true;
    }
}
