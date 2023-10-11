package template;

/**
 * Дата: 12 марта 2020 г., 9:00:13
 */

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Осуществите замену текста, таблиц и изображений в файлах docx - шаблоны поддерживают замену тегов {key}
 */

public class WordTemplate {

    private XWPFDocument document;

    /**
     * Инициализировать содержимое шаблона
     */
    public WordTemplate(InputStream inputStream) throws IOException {
        setDocument(inputStream);
    }

    public WordTemplate(String filename) throws IOException {
        setDocument(filename);
    }

    /**
     * Вставить картинку
     */
    public static void addPicture(XWPFDocument doc, XWPFRun r, String imgFile, int picWidth, int picHeight) {
        int format = 0;
        String extend = imgFile.substring(imgFile.lastIndexOf('.') + 1);
        switch (extend) {
            case "emf" -> format = XWPFDocument.PICTURE_TYPE_EMF;
            case "wmf" -> format = XWPFDocument.PICTURE_TYPE_WMF;
            case "pict" -> format = XWPFDocument.PICTURE_TYPE_PICT;
            case "jpeg", "jpg" -> format = XWPFDocument.PICTURE_TYPE_JPEG;
            case "png" -> format = XWPFDocument.PICTURE_TYPE_PNG;
            case "dib" -> format = XWPFDocument.PICTURE_TYPE_DIB;
            case "gif" -> format = XWPFDocument.PICTURE_TYPE_GIF;
            case "tiff" -> format = XWPFDocument.PICTURE_TYPE_TIFF;
            case "eps" -> format = XWPFDocument.PICTURE_TYPE_EPS;
            case "bmp" -> format = XWPFDocument.PICTURE_TYPE_BMP;
            case "wpg" -> format = XWPFDocument.PICTURE_TYPE_WPG;
            default ->
                    System.err.println("Unsupported picture: " + imgFile + ". Expected emf|wmf|pict|jpeg|png|dib|gif|tiff|eps|bmp|wpg");
        }
//        if (imgFile.endsWith(".emf")) format = XWPFDocument.PICTURE_TYPE_EMF;
//        else if (imgFile.endsWith(".wmf")) format = XWPFDocument.PICTURE_TYPE_WMF;
//        else if (imgFile.endsWith(".pict")) format = XWPFDocument.PICTURE_TYPE_PICT;
//        else if (imgFile.endsWith(".jpeg") || imgFile.endsWith(".jpg")) format = XWPFDocument.PICTURE_TYPE_JPEG;
//        else if (imgFile.endsWith(".png")) format = XWPFDocument.PICTURE_TYPE_PNG;
//        else if (imgFile.endsWith(".dib")) format = XWPFDocument.PICTURE_TYPE_DIB;
//        else if (imgFile.endsWith(".gif")) format = XWPFDocument.PICTURE_TYPE_GIF;
//        else if (imgFile.endsWith(".tiff")) format = XWPFDocument.PICTURE_TYPE_TIFF;
//        else if (imgFile.endsWith(".eps")) format = XWPFDocument.PICTURE_TYPE_EPS;
//        else if (imgFile.endsWith(".bmp")) format = XWPFDocument.PICTURE_TYPE_BMP;
//        else if (imgFile.endsWith(".wpg")) format = XWPFDocument.PICTURE_TYPE_WPG;
//        else {
//            System.err.println("Unsupported picture: " + imgFile + ". Expected emf|wmf|pict|jpeg|png|dib|gif|tiff|eps|bmp|wpg");
//        }
        try {
            r.addPicture(new FileInputStream(imgFile), format, imgFile, Units.toEMU(picWidth), Units.toEMU(picHeight));
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public XWPFDocument getDocument() {
        return document;
    }

    public void setDocument(XWPFDocument document) {
        this.document = document;
    }

    public void setDocument(InputStream inputStream) throws IOException {
        setDocument(new XWPFDocument(inputStream));
    }

    public void setDocument(String filename) throws IOException {
        setDocument(new FileInputStream(filename));
    }

    /**
     * Записать обработанный контент в выходной поток
     */
    public void write(OutputStream outputStream) throws IOException {
        document.write(outputStream);
    }

    /**
     * Заменить теги в текстовом файле согласно dataMap;
     * ! ! ! ! *** Обратите внимание на формат данных dataMap ***! ! ! !
     * Для обычной метки данных метка, которую необходимо заменить (нет необходимости в цикле) ----- карта с ключом parametersMap должна быть сохранена в dataMap,
     * Для хранения данных, которые не нужно генерировать в цикле, таких как информация заголовка, дата, производитель часов и т. Д.
     * Для данных таблицы, которые необходимо генерировать циклически ------ настройка ключа, значение - List <Map <String, Object >>
     */
    public void replaceDocument(Map<String, Object> dataMap) {

        if (!dataMap.containsKey("parametersMap")) {
            System.out.println("Источник данных ошибка-источник данных (parametersMap) отсутствует");
            return;
        }
        @SuppressWarnings("unchecked") Map<String, Object> parametersMap = (Map<String, Object>) dataMap.get("parametersMap");

        List<IBodyElement> bodyElements = document.getBodyElements();// Все объекты (абзац + таблица)
        int templateBodySize = bodyElements.size();// Отмечаем общее количество файлов шаблона (абзац + таблица)

        int curT = 0;// Индекс текущего объекта операционной таблицы
        int curP = 0;// Индекс текущего работающего объекта абзаца
        for (int i = 0; i < templateBodySize; i++) {
            if (BodyElementType.TABLE.equals(bodyElements.get(i).getElementType())) {// Обрабатываем форму
                XWPFTable table;

                List<XWPFTable> tables = bodyElements.get(i).getBody().getTables();
                table = tables.get(curT);
                if (table != null) {

                    // Обрабатываем форму
                    List<XWPFTableCell> tableCells = table.getRows().get(0).getTableCells();// Получаем первую строку таблицы шаблона для определения типа таблицы
                    String tableText = table.getText();// Весь текст в таблице

                    if (tableText.contains("foreach")) {
                        String tableType = tableCells.get(0).getText();
                        String dataSource = tableCells.get(1).getText();
                        System.out.println("Читать в источник данных:" + dataSource);
                        if (!dataMap.containsKey(dataSource)) {
                            System.out.println("Первое в документе" + (curT + 1) + "Отсутствует источник данных шаблона таблицы");
                            return;
                        }
                        @SuppressWarnings("unchecked") List<Map<String, Object>> tableDataList = (List<Map<String, Object>>) dataMap.get(dataSource);
                        if ("##{foreachTable}##".equals(tableType)) {
                            // System.out.println ("Циклическое создание таблицы");
                            addTableInDocFooter(table, tableDataList, parametersMap, 1);

                        } else if ("##{foreachTableRow}##".equals(tableType)) {
                            // System.out.println («Циклически генерировать строки внутри таблицы»);
                            addTableInDocFooter(table, tableDataList, parametersMap, 2);
                        }

                    } else if (tableText.contains("{")) {
                        // Тег ## {foreach не найден, но тег {} обычных данных замены найден. Таблицу нужно только заменить
                        addTableInDocFooter(table, null, parametersMap, 3);
                    } else {
                        // Теги не найдены, таблица статическая, просто скопируйте ее.
                        addTableInDocFooter(table, null, null, 0);
                    }
                    curT++;
                }
            } else if (BodyElementType.PARAGRAPH.equals(bodyElements.get(i).getElementType())) {// Обрабатываем абзац
                // System.out.println ("Получить абзац");
                XWPFParagraph ph = bodyElements.get(i).getBody().getParagraphArray(curP);
                if (ph != null) {
                    addParagraphInDocFooter(ph, null, parametersMap);
                    curP++;
                }
            }

        }
        // После обработки шаблона удаляем содержимое шаблона в тексте
        for (int a = 0; a < templateBodySize; a++) {
            document.removeBodyElement(0);
        }

    }

    /**
     * Создайте таблицу в конце текстового документа в соответствии с таблицей шаблона и списком данных
     * flag = (0 - статическая таблица, 1 - общий цикл таблицы, 2 - цикл внутренней строки таблицы, 3 - таблица не циклически повторяется, просто замените метку)
     */
    public void addTableInDocFooter(XWPFTable templateTable, List<Map<String, Object>> list, Map<String, Object> parametersMap, int flag) {

        if (flag == 1) {// Общий цикл таблицы
            for (Map<String, Object> map : list) {
                List<XWPFTableRow> templateTableRows = templateTable.getRows();// Получаем все строки таблицы шаблона
                XWPFTable newCreateTable = document.createTable();// Создаем новую таблицу, одну строку и один столбец по умолчанию
                for (int i = 1; i < templateTableRows.size(); i++) {
                    XWPFTableRow newCreateRow = newCreateTable.createRow();
                    CopyTableRow(newCreateRow, templateTableRows.get(i));// Копируем текст и стиль строки шаблона в новую строку
                }
                newCreateTable.removeRow(0);// удаляем первую лишнюю строку
                document.createParagraph();// Добавляем возврат каретки и перевод строки
                replaceTable(newCreateTable, map);// Заменить ярлык
            }

        } else if (flag == 2) {// Внутренний цикл строк таблицы table
            XWPFTable newCreateTable = document.createTable();// Создаем новую таблицу, одну строку и один столбец по умолчанию
            List<XWPFTableRow> TempTableRows = templateTable.getRows();// Получаем все строки таблицы шаблона
            int tagRowsIndex = 0;// помечаем индексы строк
            for (int i = 0, size = TempTableRows.size(); i < size; i++) {
                String rowText = TempTableRows.get(i).getCell(0).getText();// Получаем первую ячейку строки таблицы
                if (rowText.contains("##{foreachRows}##")) {
                    tagRowsIndex = i;
                    break;
                }
            }

            /*Копируем строку шаблона и строку перед строкой метки */
            for (int i = 1; i < tagRowsIndex; i++) {
                XWPFTableRow newCreateRow = newCreateTable.createRow();
                CopyTableRow(newCreateRow, TempTableRows.get(i));// копируем строку
                replaceTableRow(newCreateRow, parametersMap);// Обрабатываем замену некруглых тегов
            }

            /*Циклически генерировать строки шаблона */
            XWPFTableRow tempRow = TempTableRows.get(tagRowsIndex + 1);// Получаем строку шаблона
            for (int i = 0; i < list.size(); i++) {
                XWPFTableRow newCreateRow = newCreateTable.createRow();
                CopyTableRow(newCreateRow, tempRow);// Копируем строку шаблона
                replaceTableRow(newCreateRow, list.get(i));// обрабатываем замену метки
            }

            /*Копируем строку шаблона и строку после строки метки */
            for (int i = tagRowsIndex + 2; i < TempTableRows.size(); i++) {
                XWPFTableRow newCreateRow = newCreateTable.createRow();
                CopyTableRow(newCreateRow, TempTableRows.get(i));// копируем строку
                replaceTableRow(newCreateRow, parametersMap);// Обрабатываем замену некруглых тегов
            }
            newCreateTable.removeRow(0);// удаляем первую лишнюю строку
            document.createParagraph();// Добавляем возврат каретки и перевод строки

        } else if (flag == 3) {
            // Таблица не зацикливается, а просто заменяет метку
            List<XWPFTableRow> templateTableRows = templateTable.getRows();// Получаем все строки таблицы шаблона
            XWPFTable newCreateTable = document.createTable();// Создаем новую таблицу, одну строку и один столбец по умолчанию
            for (int i = 0; i < templateTableRows.size(); i++) {
                XWPFTableRow newCreateRow = newCreateTable.createRow();
                CopyTableRow(newCreateRow, templateTableRows.get(i));// Копируем текст и стиль строки шаблона в новую строку
            }
            newCreateTable.removeRow(0);// удаляем первую лишнюю строку
            document.createParagraph();// Добавляем возврат каретки и перевод строки
            replaceTable(newCreateTable, parametersMap);

        } else if (flag == 0) {
            List<XWPFTableRow> templateTableRows = templateTable.getRows();// Получаем все строки таблицы шаблона
            XWPFTable newCreateTable = document.createTable();// Создаем новую таблицу, одну строку и один столбец по умолчанию
            for (int i = 0; i < templateTableRows.size(); i++) {
                XWPFTableRow newCreateRow = newCreateTable.createRow();
                CopyTableRow(newCreateRow, templateTableRows.get(i));// Копируем текст и стиль строки шаблона в новую строку
            }
            newCreateTable.removeRow(0);// удаляем первую лишнюю строку
            document.createParagraph();// Добавляем возврат каретки и перевод строки
        }

    }

    /**
     * Сгенерировать абзац в конце документа согласно абзацу шаблона и данным
     */
    public void addParagraphInDocFooter(XWPFParagraph templateParagraph, List<Map<String, String>> list, Map<String, Object> parametersMap) {

        XWPFParagraph createParagraph = document.createParagraph();
        // Устанавливаем стиль абзаца
        createParagraph.getCTP().setPPr(templateParagraph.getCTP().getPPr());
        // Удаляем исходный контент
        for (int pos = 0; pos < createParagraph.getRuns().size(); pos++) {
            createParagraph.removeRun(pos);
        }
        // Добавляем тег Run
        for (XWPFRun s : templateParagraph.getRuns()) {
            XWPFRun targetrun = createParagraph.createRun();
            CopyRun(targetrun, s);
        }
        // Заменить данные абзаца
        replaceParagraph(createParagraph, parametersMap);

    }

    /**
     * Заменить теги {**} в элементах абзаца согласно карте
     */
    public void replaceParagraph(XWPFParagraph xWPFParagraph, Map<String, Object> parametersMap) {
        List<XWPFRun> runs = xWPFParagraph.getRuns();
        String xWPFParagraphText = xWPFParagraph.getText();
        String regEx = "\\{.+?\\}";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(xWPFParagraphText);// Обычная строка соответствия {****}

        if (matcher.find()) {
            // Выполняем замену после нахождения тега
            int beginRunIndex = xWPFParagraph.searchText("{", new PositionInParagraph()).getBeginRun();// начальная позиция метки
            int endRunIndex = xWPFParagraph.searchText("}", new PositionInParagraph()).getEndRun();// конечный тег
            StringBuffer key = new StringBuffer();

            if (beginRunIndex == endRunIndex) {
                // {**} В теге выполнения
                XWPFRun beginRun = runs.get(beginRunIndex);
                String beginRunText = beginRun.text();

                int beginIndex = beginRunText.indexOf("{");
                int endIndex = beginRunText.indexOf("}");
                int length = beginRunText.length();

                if (beginIndex == 0 && endIndex == length - 1) {
                    // В теге выполнения есть только {**}
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(beginRunIndex);
                    insertNewRun.getCTR().setRPr(beginRun.getCTR().getRPr());
                    // устанавливаем текст
                    key.append(beginRunText, 1, endIndex);
                    insertNewRun.setText(getValueByKey(key.toString(), parametersMap));
                    xWPFParagraph.removeRun(beginRunIndex + 1);
                } else {
                    // Тег выполнения - ** {**} ** или ** {**} или {**} **, после замены ключа вам также необходимо добавить текст до и после исходного ключа
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(beginRunIndex);
                    insertNewRun.getCTR().setRPr(beginRun.getCTR().getRPr());
                    // устанавливаем текст
                    key.append(beginRunText, beginRunText.indexOf("{") + 1, beginRunText.indexOf("}"));
                    String textString = beginRunText.substring(0, beginIndex) + getValueByKey(key.toString(), parametersMap) + beginRunText.substring(endIndex + 1);
                    insertNewRun.setText(textString);
                    xWPFParagraph.removeRun(beginRunIndex + 1);
                }

            } else {
                // {**} делится на несколько прогонов
                // Сначала обрабатываем начальный тег выполнения и получаем первое значение {key}
                XWPFRun beginRun = runs.get(beginRunIndex);
                String beginRunText = beginRun.text();
                int beginIndex = beginRunText.indexOf("{");
                if (beginRunText.length() > 1) {
                    key.append(beginRunText.substring(beginIndex + 1));
                }
                ArrayList<Integer> removeRunList = new ArrayList<>();// Прогон, который нужно удалить
                // Обрабатываем средний прогон
                for (int i = beginRunIndex + 1; i < endRunIndex; i++) {
                    XWPFRun run = runs.get(i);
                    String runText = run.text();
                    key.append(runText);
                    removeRunList.add(i);
                }

                // Получаем значение ключа в endRun
                XWPFRun endRun = runs.get(endRunIndex);
                String endRunText = endRun.text();
                int endIndex = endRunText.indexOf("}");
                // запускаем **) или **) **
                if (endRunText.length() > 1 && endIndex != 0) {
                    key.append(endRunText, 0, endIndex);
                }


                //*******************************************************************
                // Заменять метку после получения значения ключа

                // Сначала обрабатываем начальный тег
                if (beginRunText.length() == 2) {
                    // текст в теге выполнения {
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(beginRunIndex);
                    insertNewRun.getCTR().setRPr(beginRun.getCTR().getRPr());
                    // устанавливаем текст
                    insertNewRun.setText(getValueByKey(key.toString(), parametersMap));
                    xWPFParagraph.removeRun(beginRunIndex + 1);// Удаляем исходный прогон
                } else {
                    // Тег выполнения - ** {** или {**, после замены ключа вам также необходимо добавить текст перед исходным ключом
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(beginRunIndex);
                    insertNewRun.getCTR().setRPr(beginRun.getCTR().getRPr());
                    // устанавливаем текст
                    String textString = beginRunText.substring(0, beginRunText.indexOf("{")) + getValueByKey(key.toString(), parametersMap);
                    // Определяем, является ли это суффиксом изображения, если он должен загрузить изображение, в противном случае заменяем текст
                    if (textString.endsWith(".emf") || textString.endsWith(".wmf") || textString.endsWith(".pict") || textString.endsWith(".jpeg") || textString.endsWith(".jpg") || textString.endsWith(".png") || textString.endsWith(".dib") || textString.endsWith(".gif") || textString.endsWith(".tiff") || textString.endsWith(".eps") || textString.endsWith(".eps") || textString.endsWith(".bmp") || textString.endsWith(".wpg")) {

                        int width = 100;    // Размер по умолчанию
                        int height = 100;
                        // Устанавливаем желаемую длину и ширину картинки
                        if (textString.equals("файл \\ маленькая девочка.jpg")) {
                            width = 300;
                            height = 250;
                        }
                        if (textString.equals("файл \\ Little Wukong.png")) {
                            width = 200;
                            height = 250;
                        }

                        // Загрузить картинку
                        addPicture(document, insertNewRun, textString, width, height);  // Вставляем картинку
                    } else {
                        insertNewRun.setText(textString); // Вставляем текст
                    }
                    xWPFParagraph.removeRun(beginRunIndex + 1);// Удаляем исходный прогон
                }


                // Обработка конечного тега
                if (endRunText.length() == 1) {
                    // Только текст в теге запуска}
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(endRunIndex);
                    insertNewRun.getCTR().setRPr(endRun.getCTR().getRPr());
                    // устанавливаем текст
                    insertNewRun.setText("");
                    xWPFParagraph.removeRun(endRunIndex + 1);// Удаляем исходный прогон

                } else {
                    // Тег выполнения - **} ** или} ** или **}, после замены ключа вам также необходимо добавить текст после исходного ключа
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(endRunIndex);
                    insertNewRun.getCTR().setRPr(endRun.getCTR().getRPr());
                    // устанавливаем текст
                    String textString = endRunText.substring(endRunText.indexOf("}") + 1);
                    insertNewRun.setText(textString);
                    xWPFParagraph.removeRun(endRunIndex + 1);// Удаляем исходный прогон
                }

                // Обрабатываем средний тег выполнения
                for (Integer integer : removeRunList) {
                    XWPFRun xWPFRun = runs.get(integer);// Исходный запуск
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(integer);
                    insertNewRun.getCTR().setRPr(xWPFRun.getCTR().getRPr());
                    insertNewRun.setText("");
                    xWPFParagraph.removeRun(integer + 1);// Удаляем исходный прогон

                }

            }// Обработка $ {**} делится на несколько прогонов

            replaceParagraph(xWPFParagraph, parametersMap);

        }// если есть метка

    }

    /**
     * Копировать строку таблицы в формате XWPFTableRow
     *
     * @param target XWPFTableRow в формате, который нужно изменить
     * @param source Шаблон XWPFTableRow
     */
    private void CopyTableRow(XWPFTableRow target, XWPFTableRow source) {

        int tempRowCellsize = source.getTableCells().size();// Количество столбцов в строке шаблона
        for (int i = 0; i < tempRowCellsize - 1; i++) {
            target.addNewTableCell();// Для вновь добавленной строки добавляем то же количество ячеек, что и соответствующая строка таблицы шаблона
        }
        // Копируем стиль
        target.getCtRow().setTrPr(source.getCtRow().getTrPr());
        // копируем ячейку
        for (int i = 0; i < target.getTableCells().size(); i++) {
            copyTableCell(target.getCell(i), source.getCell(i));
        }
    }

    /**
     * Скопируйте ячейку в формате XWPFTableCell
     *
     * @param newTableCell      вновь созданная ячейка
     * @param templateTableCell ячейка шаблона
     */
    private void copyTableCell(XWPFTableCell newTableCell, XWPFTableCell templateTableCell) {
        // атрибуты столбца
        newTableCell.getCTTc().setTcPr(templateTableCell.getCTTc().getTcPr());
        // Удаляем все абзацы текста в targetCell
        for (int pos = 0; pos < newTableCell.getParagraphs().size(); pos++) {
            newTableCell.removeParagraph(pos);
        }
        // Добавить новый текстовый абзац
        for (XWPFParagraph sp : templateTableCell.getParagraphs()) {
            XWPFParagraph targetP = newTableCell.addParagraph();
            copyParagraph(targetP, sp);
        }
    }

    /**
     * Копировать текстовые абзацы в формате XWPFParagraph
     *
     * @param newParagraph      вновь созданный абзац
     * @param templateParagraph абзац шаблона
     */
    private void copyParagraph(XWPFParagraph newParagraph, XWPFParagraph templateParagraph) {
        // Устанавливаем стиль абзаца
        newParagraph.getCTP().setPPr(templateParagraph.getCTP().getPPr());
        // Добавляем тег Run
        for (int pos = 0; pos < newParagraph.getRuns().size(); pos++) {
            newParagraph.removeRun(pos);

        }
        for (XWPFRun s : templateParagraph.getRuns()) {
            XWPFRun targetrun = newParagraph.createRun();
            CopyRun(targetrun, s);
        }

    }

    /**
     * Скопируйте текстовый узел run
     *
     * @param newRun      - запустить только что созданный текстовый узел
     * @param templateRun - запустить текстовый узел шаблона
     */
    private void CopyRun(XWPFRun newRun, XWPFRun templateRun) {
        newRun.getCTR().setRPr(templateRun.getCTR().getRPr());
        // устанавливаем текст
        newRun.setText(templateRun.text());
    }

    /**
     * Заменить метку на строке таблицы согласно параметрам Карта
     *
     * @param tableRow      строка таблицы
     * @param parametersMap параметры карты
     */
    public void replaceTableRow(XWPFTableRow tableRow, Map<String, Object> parametersMap) {

        List<XWPFTableCell> tableCells = tableRow.getTableCells();
        for (XWPFTableCell xWPFTableCell : tableCells) {
            List<XWPFParagraph> paragraphs = xWPFTableCell.getParagraphs();
            for (XWPFParagraph xwpfParagraph : paragraphs) {

                replaceParagraph(xwpfParagraph, parametersMap);
            }
        }

    }

    /**
     * Заменить тег {key} в таблице согласно карте
     */
    public void replaceTable(XWPFTable xwpfTable, Map<String, Object> parametersMap) {
        List<XWPFTableRow> rows = xwpfTable.getRows();
        for (XWPFTableRow xWPFTableRow : rows) {
            List<XWPFTableCell> tableCells = xWPFTableRow.getTableCells();
            for (XWPFTableCell xWPFTableCell : tableCells) {
                List<XWPFParagraph> paragraphs2 = xWPFTableCell.getParagraphs();
                for (XWPFParagraph xWPFParagraph : paragraphs2) {
                    replaceParagraph(xWPFParagraph, parametersMap);
                }
            }
        }

    }

    private String getValueByKey(String key, Map<String, Object> map) {
//        String returnValue = "";
        if (key != null) {
            try {
                return map.get(key) != null ? map.get(key).toString() : "";
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("key:" + key + "***" + e);
                return "";
            }
        }
        return "";
    }

}
