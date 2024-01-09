package trade_company.report;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import template.WordTemplate;
import trade_company.logic.sql_object.*;
import trade_company.models.UserDataModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Math.floor;
import static java.lang.Math.min;

public class reportClass {
    static final String DEFAULT_PATH = "user.dir";
    private static String lastPath = "user.dir";
    final int okpo_code = 777;
    final int okdp_code = 1203012;
    final String Organisation_name = "ООО «Торговая фирма»";

    public static String requestSaveFolderFromUser(String formName) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выберите папку для сохранения формы " + formName);

        File initialDirectory;
        if (lastPath.equals(DEFAULT_PATH)) initialDirectory = new File(System.getProperty("user.dir"));
        else initialDirectory = new File(lastPath);

        directoryChooser.setInitialDirectory(initialDirectory);

        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if (selectedDirectory != null) {
            lastPath = selectedDirectory.getAbsolutePath();
            return lastPath;
        }
        return null;
    }

    public void makeM4(int orderNumber, Supplier supplier, Warehouse warehouse, ArrayList<Availability> avails, Person accepted, String path_to_save) throws IOException {
        WordTemplate wordTemplate = new WordTemplate(Objects.requireNonNull(this.getClass().getResource("/template/М-4.docx")).openStream());
        Map<String, Object> wordDataMap = new HashMap<String, Object>();  // Сохраняем все данные отчета
        Map<String, Object> parametersMap = new HashMap<String, Object>();// Сохраняем данные, которые не зацикливаются в отчете
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");  // Устанавливаем формат даты
        parametersMap.put("Order_number", orderNumber);
        parametersMap.put("Date", df.format(new Date()));
        parametersMap.put("Organisation_name", Organisation_name);
        parametersMap.put("Structural_division", warehouse.getName());
        parametersMap.put("OKPO_code", okpo_code);
        parametersMap.put("Supplier_name", supplier.getName());
        parametersMap.put("Supplier_code", supplier.getOkpo());
        parametersMap.put("Insurance_company", "ИнгосСтрах");
        parametersMap.put("Corresponding_account", "515");
        parametersMap.put("Analytical_accounting_code", "314");
        parametersMap.put("Number_of_the_accompanying_document", 2);
        parametersMap.put("Payment_document_number", supplier.getPaymentAccount());
        parametersMap.put("Accepted_the_position", accepted.getPost().getName());
        parametersMap.put("Accepted_signature", accepted.getFIO());
        List<Map<String, Object>> ProductsTable = new ArrayList<>();
        double sum = 0;
        for (Availability avail : avails) {
            Map<String, Object> prod = new HashMap<>();
            prod.put("Name", avail.getProduct().getName());
            prod.put("Numenclature", avail.getProduct().getNomenclature());
            prod.put("OKEI_code", avail.getProduct().getOkei().getId());
            prod.put("OKEI_name", avail.getProduct().getOkei().getName());
            prod.put("Count", avail.getCount());
            prod.put("Accepted", avail.getCount());
            prod.put("Price", avail.getPrice());
            double fullPrice = avail.getPrice() * avail.getCount();
            sum += fullPrice;
            prod.put("Full_price_without_NDS", fullPrice * (1. - (((double) avail.getProduct().getPercentNDS()) / 100)));
            prod.put("Full_price_NDS", fullPrice * (((double) avail.getProduct().getPercentNDS()) / 100.));
            prod.put("Full_price", fullPrice);
            prod.put("Passport_number", "");
            prod.put("Number", avail.getId());
            ProductsTable.add(prod);
        }
        parametersMap.put("Sum_price", sum);
        wordDataMap.put("product", ProductsTable);
        wordDataMap.put("parametersMap", parametersMap);
        wordTemplate.replaceDocument(wordDataMap);
        File outputFile = new File(path_to_save);
        FileOutputStream fos = new FileOutputStream(outputFile + ".docx");
        wordTemplate.getDocument().write(fos);
    }

    public void makeTORG11(int DocNumber, ArrayList<Availability> avails, Person s, String path_to_save) throws IOException {
        WordTemplate wordTemplate = new WordTemplate(Objects.requireNonNull(this.getClass().getResource("/template/ТОРГ-11.docx")).openStream());
        Map<String, Object> wordDataMap = new HashMap<String, Object>();  // Сохраняем все данные отчета
        Map<String, Object> parametersMap = new HashMap<String, Object>();// Сохраняем данные, которые не зацикливаются в отчете
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");  // Устанавливаем формат даты
        parametersMap.put("Date", df.format(new Date()));
        df = new SimpleDateFormat("dd");
        parametersMap.put("Day", df.format(new Date()));
        df = new SimpleDateFormat("MM");
        parametersMap.put("Month", df.format(new Date()));
        df = new SimpleDateFormat("yyyy");
        parametersMap.put("Year", df.format(new Date()));
        parametersMap.put("Doc_number", DocNumber);
        parametersMap.put("Organisation_name", "ООО «Торговая фирма»");
        parametersMap.put("Structural_division", UserDataModel.getInstance().getWarehouse().getName());
        parametersMap.put("OKPO_code", okpo_code);
        parametersMap.put("Type_of_OKDP_activity", okdp_code);
        parametersMap.put("Operation_code", "23");
        int size = min(avails.size(), 7);
        int sum = 0;
        for (int i = 0; i < size; i++) {
            parametersMap.put("Product_name" + i, avails.get(i).getProduct().getName());
            parametersMap.put("Product_code" + i, avails.get(i).getProduct().getId());
            parametersMap.put("OKEI_name" + i, avails.get(i).getProduct().getOkei().getName());
            parametersMap.put("OKEI_code" + i, avails.get(i).getProduct().getOkei().getId());
            parametersMap.put("Article" + i, avails.get(i).getProduct().getArticle());
            parametersMap.put("Size" + i, avails.get(i).getWidth() + "x" + avails.get(i).getHeight() + "x" + avails.get(i).getDepth());
            parametersMap.put("Count" + i, avails.get(i).getCount());
            sum += avails.get(i).getCount();
            parametersMap.put("Price" + i, avails.get(i).getPrice());
        }
        for (int i = size; i < 6; i++) {
            parametersMap.put("Product_name" + i, "");
            parametersMap.put("Product_code" + i, "");
            parametersMap.put("OKEI_name" + i, "");
            parametersMap.put("OKEI_code" + i, "");
            parametersMap.put("Article" + i, "");
            parametersMap.put("Size" + i, "");
            parametersMap.put("Count" + i, "");
            parametersMap.put("Price" + i, "");
        }
        parametersMap.put("Sum_count", sum);
        parametersMap.put("Count_in_word", "");
        parametersMap.put("Post0", "");
        parametersMap.put("Post1", s.getPost().getName());
        parametersMap.put("Signature0", "");
        parametersMap.put("Signature1", s.getFIO());

        wordDataMap.put("parametersMap", parametersMap);
        wordTemplate.replaceDocument(wordDataMap);
        File outputFile = new File(path_to_save);
        FileOutputStream fos = new FileOutputStream(outputFile + ".docx");
        wordTemplate.getDocument().write(fos);
    }

    public void makeM15(int orderNumber, ArrayList<Availability> avails, Supplier supplier, String path_to_save) throws IOException {
        WordTemplate wordTemplate = new WordTemplate(Objects.requireNonNull(this.getClass().getResource("/template/М-15.docx")).openStream());
        Map<String, Object> wordDataMap = new HashMap<String, Object>();  // Сохраняем все данные отчета
        Map<String, Object> parametersMap = new HashMap<String, Object>();// Сохраняем данные, которые не зацикливаются в отчете
        parametersMap.put("Doc_number", orderNumber);
        parametersMap.put("Organisation_name", Organisation_name);
        parametersMap.put("OKPO_code", okpo_code);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");  // Устанавливаем формат даты
        parametersMap.put("Date", df.format(new Date()));
        parametersMap.put("Operation_code", 123);
        parametersMap.put("Sender1", UserDataModel.getInstance().getWarehouse().getName());
        parametersMap.put("Sender2", "");
        parametersMap.put("Getter1", supplier.getName());
        parametersMap.put("Getter2", "");
        parametersMap.put("Responsible_for_delivery1", "");
        parametersMap.put("Responsible_for_delivery2", "");
        parametersMap.put("Responsible_for_delivery3", "");
        parametersMap.put("Base", "");
        parametersMap.put("Whom", supplier.getName());
        parametersMap.put("Through_whom", "");

        int size = min(29, avails.size());
        double sumPrice = 0;
        double sumNDSPrice = 0;
        for (int i = 0; i < size; i++) {
            parametersMap.put("Account" + i, "");
            parametersMap.put("Analytical_accounting_code" + i, "");

            Product product = avails.get(i).getProduct();
            parametersMap.put("Name" + i, product.getName());
            parametersMap.put("Nomenclature" + i, product.getNomenclature());
            OKEI okei = product.getOkei();
            parametersMap.put("OKEI_code" + i, okei.getId());
            parametersMap.put("OKEI_name" + i, okei.getName());

            parametersMap.put("Need_to_release" + i, avails.get(i).getCount());
            parametersMap.put("Released" + i, avails.get(i).getCount());

            double price = avails.get(i).getPrice();
            double nds = ((double) avails.get(i).getProduct().getPercentNDS()) / 100.;
            parametersMap.put("Price" + i, price);
            price *= avails.get(i).getCount();
            parametersMap.put("Price_without_NDS" + i, price * (1 - nds));
            parametersMap.put("Price_NDS" + i, price * nds);
            parametersMap.put("Full_price" + i, price);

            parametersMap.put("Invent_number" + i, "");
            parametersMap.put("Passport_number" + i, "");
            parametersMap.put("Number" + i, "");
        }

        for (int i = size; i < 29; i++) {
            parametersMap.put("Account" + i, "");
            parametersMap.put("Analytical_accounting_code" + i, "");
            parametersMap.put("Name" + i, "");
            parametersMap.put("Nomenclature" + i, "");
            parametersMap.put("OKEI_code" + i, "");
            parametersMap.put("OKEI_name" + i, "");
            parametersMap.put("Need_to_release" + i, "");
            parametersMap.put("Released" + i, "");
            parametersMap.put("Price" + i, "");
            parametersMap.put("Price_without_NDS" + i, "");
            parametersMap.put("Price_NDS" + i, "");
            parametersMap.put("Full_price" + i, "");
            parametersMap.put("Invent_number" + i, "");
            parametersMap.put("Passport_number" + i, "");
            parametersMap.put("Number" + i, "");
        }
        parametersMap.put("Released_in_word", "");
        parametersMap.put("Sum_price_in_wordR", floor(sumPrice));
        parametersMap.put("Sum_price_in_wordC", floor(sumPrice * 100) % 100);
        parametersMap.put("Sum_NDSR", floor(sumNDSPrice));
        parametersMap.put("Sum_NDSC", floor(sumNDSPrice * 100) % 100);
        parametersMap.put("Post_rel", UserDataModel.getInstance().getPerson().getPost().getName());
        parametersMap.put("Signature_rel", UserDataModel.getInstance().getPerson().getFIO());

        wordDataMap.put("parametersMap", parametersMap);
        wordTemplate.replaceDocument(wordDataMap);
        File outputFile = new File(path_to_save);
        FileOutputStream fos = new FileOutputStream(outputFile + ".docx");
        wordTemplate.getDocument().write(fos);
    }

    public void makeM7(int orderNumber, ArrayList<Availability> avails, Person approve, Warehouse warehouse, Supplier supplier, String path_to_save) throws IOException {
        WordTemplate wordTemplate = new WordTemplate(Objects.requireNonNull(this.getClass().getResource("/template/М-15.docx")).openStream());
        Map<String, Object> wordDataMap = new HashMap<String, Object>();  // Сохраняем все данные отчета
        Map<String, Object> parametersMap = new HashMap<String, Object>();// Сохраняем данные, которые не зацикливаются в отчете

        parametersMap.put("Act_number", orderNumber);
        parametersMap.put("Approve_the_position", approve.getPost().getName());
        parametersMap.put("Approve_signature", approve.getFIO());
        SimpleDateFormat df = new SimpleDateFormat("dd");  // Устанавливаем формат даты
//        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");  // Устанавливаем формат даты
        parametersMap.put("Day", df.format(new Date()));
        df = new SimpleDateFormat("MM");
        parametersMap.put("Month", df.format(new Date()));
        df = new SimpleDateFormat("yy");
        parametersMap.put("Year", df.format(new Date()));
        df = new SimpleDateFormat("yyyy");
        parametersMap.put("Full_year", df.format(new Date()));
        parametersMap.put("Organisation_name", Organisation_name);
        parametersMap.put("OKPO_code", okpo_code);
        parametersMap.put("Place_of_drawing_up_the_act", warehouse.getName());

        parametersMap.put("Start_of_acceptance_hour", "");
        parametersMap.put("Start_of_acceptance_minutes", "");
        parametersMap.put("End_of_acceptance_hour", "");
        parametersMap.put("End_of_acceptance_minutes", "");
        parametersMap.put("Document_number", "");
        parametersMap.put("Quality_certificate", "");
        parametersMap.put("From_the_station", "");

        parametersMap.put("Shipper_fullname", "");
        parametersMap.put("Shipper_fulladr", "");
        parametersMap.put("Shipper_phone", "");

        parametersMap.put("Supplier_fullname", supplier.getName());
        parametersMap.put("Supplier_fulladr", supplier.getAddressFull());
        parametersMap.put("Supplier_phone", supplier.getPhone());

        parametersMap.put("Getter_fullname", warehouse.getName());
        parametersMap.put("Getter_fulladr", warehouse.getStreet() + ", " + warehouse.getStreet() + ", " + warehouse.getBuildingNumber() + warehouse.getBuildingLiteral());
        parametersMap.put("Getter_phone", "");

        parametersMap.put("Insurance_company", "ИнгосСтрах");
        StringBuilder Storage_conditions = new StringBuilder();
        int size = min(9, avails.size());
        for (int i = 0; i < size; i++) {
            Availability availability = avails.get(i);
            parametersMap.put("Count" + i, availability.getCount());
            parametersMap.put("PackageType" + i, availability.getPackageType());
            parametersMap.put("ProductName" + i, availability.getProduct().getName());
            parametersMap.put("Massure" + i, availability.getProduct().getOkei().getName());
            parametersMap.put("Mass" + i, availability.getProduct().getMass() * availability.getCount() / 1000);
            Storage_conditions.append(availability.getProduct().getName()).append(" хранится при ").append(availability.getProduct().getStorageConditions()).append(";");
        }
        for (int i = size; i < 9; i++) {
            parametersMap.put("Count" + i, "");
            parametersMap.put("PackageType" + i, "");
            parametersMap.put("ProductName" + i, "");
            parametersMap.put("Massure" + i, "");
            parametersMap.put("Mass" + i, "");
        }

        parametersMap.put("Storage_conditions", Storage_conditions.toString());
        parametersMap.put("Operation_code", "123");
        parametersMap.put("Structure", "");
        parametersMap.put("Deal_type", "");
        parametersMap.put("Warehouse", warehouse.getName());
        parametersMap.put("Supplier_code", supplier.getOkpo());
        size = min(avails.size(), 19);
        for (int i = 0; i < size; i++) {
            Availability availability = avails.get(i);
            parametersMap.put("Product" + i, availability.getProduct().getName());
            parametersMap.put("Nomenclature" + i, availability.getProduct().getNomenclature());
            parametersMap.put("OKEI_code" + i, availability.getProduct().getOkei().getId());
            parametersMap.put("OKEI_name" + i, availability.getProduct().getOkei().getName());
            parametersMap.put("Ccount" + i, availability.getCount());
            parametersMap.put("Price" + i, availability.getPrice());
            parametersMap.put("SumPrice" + i, availability.getPrice() * availability.getCount());
        }
        for (int i = size; i < 19; i++) {
            parametersMap.put("Product" + i, "");
            parametersMap.put("Nomenclature" + i, "");
            parametersMap.put("OKEI_code" + i, "");
            parametersMap.put("OKEI_name" + i, "");
            parametersMap.put("Ccount" + i, "");
            parametersMap.put("Price" + i, "");
            parametersMap.put("SumPrice" + i, "");
        }

        wordDataMap.put("parametersMap", parametersMap);
        wordTemplate.replaceDocument(wordDataMap);
        File outputFile = new File(path_to_save);
        FileOutputStream fos = new FileOutputStream(outputFile + ".docx");
        wordTemplate.getDocument().write(fos);
    }

    public void makeMH20(int docNumber, Warehouse warehouse, Person responsible, ArrayList<Availability> availabilities, ArrayList<Double> beginning, ArrayList<Double> coming, ArrayList<Double> expenditure, Date beginDate, Date endDate, String path_to_save) throws IOException {
        WordTemplate wordTemplate = new WordTemplate(Objects.requireNonNull(this.getClass().getResource("/template/МХ-20.docx")).openStream());
        Map<String, Object> wordDataMap = new HashMap<>();  // Сохраняем все данные отчета
        Map<String, Object> parametersMap = new HashMap<>();// Сохраняем данные, которые не зацикливаются в отчете

        SimpleDateFormat df = new SimpleDateFormat("dd");  // Устанавливаем формат даты
        parametersMap.put("Day", df.format(new Date()));
        df = new SimpleDateFormat("MM");
        parametersMap.put("Month", df.format(new Date()));
        df = new SimpleDateFormat("yy");
        parametersMap.put("Year", df.format(new Date()));
        df = new SimpleDateFormat("dd MM yyyy");
        parametersMap.put("Date", df.format(new Date()));
        parametersMap.put("Reporting_period_beginning", df.format(beginDate));
        parametersMap.put("Reporting_period_ending", df.format(endDate));

        parametersMap.put("Organisation_name", Organisation_name);
        parametersMap.put("Structural_division", warehouse.getName());
        parametersMap.put("OKDP_code", okdp_code);
        parametersMap.put("Warehouse_number", warehouse.getName());
        parametersMap.put("Cam_number", "");
        parametersMap.put("Section_number", "");
        parametersMap.put("Operation_type", 12);
        parametersMap.put("Doc_number", docNumber);
        parametersMap.put("Full_name", responsible.getFullName());

        List<Map<String, Object>> productTable = new ArrayList<>();
        int sumBegCount = 0;
        double sumBegPrice = 0;
        int sumComingCount = 0;
        double sumComingPrice = 0;
        int sumExpenditureCount = 0;
        double sumExpenditurePrice = 0;
        int sumEndCount = 0;
        double sumEndPrice = 0;
        for (int i = 0; i < availabilities.size(); i++) {
            Map<String, Object> prod = new HashMap<>();
            Availability avail = availabilities.get(i);
            prod.put("Sequence_number", i + 1);
            prod.put("Name", avail.getProduct().getName());
            prod.put("OKEI_name", avail.getProduct().getOkei().getName());
            prod.put("OKEI_code", avail.getProduct().getOkei().getId());
            double price = avail.getPrice();
            prod.put("Price", price);

            double begCount = beginning.get(i);
            sumBegCount += begCount;
            if ((int) Math.floor(begCount * 100) != 0) {
                prod.put("Beg_count", begCount);
                prod.put("Beg_full_price", begCount * price);
            } else {
                prod.put("Beg_count", "-");
                prod.put("Beg_full_price", "-");
            }
            sumBegPrice += begCount * price;

            Double comingCount = coming.get(i);
            sumComingCount += comingCount;
            if ((int) Math.floor(comingCount * 100) != 0) {
                prod.put("Coming_count", comingCount);
                prod.put("Coming_full_price", comingCount * price);
            } else {
                prod.put("Coming_count", "-");
                prod.put("Coming_full_price", "-");
            }
            sumComingPrice += comingCount * price;

            Double expenditureCount = expenditure.get(i);
            sumExpenditureCount += expenditureCount;
            if ((int) Math.floor(expenditureCount * 100) != 0) {
                prod.put("Expenditure_count", expenditureCount);
                prod.put("Expenditure_full_price", expenditureCount * price);
            } else {
                prod.put("Expenditure_count", "-");
                prod.put("Expenditure_full_price", "-");
            }
            sumExpenditurePrice += expenditureCount * price;

            double EndCount = begCount + comingCount - expenditureCount;
            sumEndCount += EndCount;
            if ((int) Math.floor(EndCount * 100) != 0) {
                prod.put("End_count", EndCount);
                prod.put("End_price", EndCount * price);
            } else {
                prod.put("End_count", "-");
                prod.put("End_price", "-");
            }
            sumEndPrice += EndCount * price;

            productTable.add(prod);
        }

        parametersMap.put("Sum_beg_count", sumBegCount);
        parametersMap.put("Sum_beg_full_price", sumBegPrice);
        parametersMap.put("Sum_coming_count", sumComingCount);
        parametersMap.put("Sum_coming_full_price", sumComingPrice);
        parametersMap.put("Sum_expenditure_count", sumExpenditureCount);
        parametersMap.put("Sum_expenditure_full_price", sumExpenditurePrice);
        parametersMap.put("Sum_end_count", sumEndCount);
        parametersMap.put("Sum_end_price", sumEndPrice);

        parametersMap.put("Count_in_words", "");
        parametersMap.put("Post_responsible", responsible.getPost().getName());
        parametersMap.put("Sign_responsible", responsible.getFIO());

        wordDataMap.put("parametersMap", parametersMap);
        wordDataMap.put("Product", productTable);
        wordTemplate.replaceDocument(wordDataMap);
        File outputFile = new File(path_to_save);
        FileOutputStream fos = new FileOutputStream(outputFile + ".docx");
        wordTemplate.getDocument().write(fos);
    }
}
