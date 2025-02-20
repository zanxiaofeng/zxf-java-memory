package zxf.java.memory.pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import zxf.java.memory.util.DebugUtils;
import zxf.java.memory.util.MemoryMonitor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PdfboxTests {
    public static void main(String[] args) throws IOException, InterruptedException {
        waitInputFromKeyboard("Please press any key for start");
        loadPdf();
        MemoryMonitor.logMemoryInfoFromMXBean("main.1");
        System.gc();
        waitInputFromKeyboard("Please press any key for end");
    }

    private static void loadPdf() throws IOException, InterruptedException {
        System.gc();
        MemoryMonitor.logMemoryInfoFromMXBean("loadPdf.start");
        //DebugUtils.callJmap("loadPdf.start");
        List<BufferedImage> results = new ArrayList<>();
        try (PDDocument pdfDocument = PDDocument.load(Paths.get("./test-pdf/developer-mozilla-org-CORS-en.pdf").toFile())) {
            MemoryMonitor.logMemoryInfoFromMXBean("loadPdf.after.load");
            //DebugUtils.callJmap("loadPdf.after.load");
            PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
            for (int pageIndex = 0; pageIndex < pdfDocument.getNumberOfPages(); pageIndex++){
                BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, 200);
                results.add(image);
                waitInputFromKeyboard("Please press any key for loadPdf.each." + pageIndex);
            }
        }
        MemoryMonitor.logMemoryInfoFromMXBean("loadPdf.end");
        waitInputFromKeyboard("Please press any key for loadPdf.end");
        //DebugUtils.callJmap("loadPdf.end");
    }

    private static void waitInputFromKeyboard(String prompt){
        Scanner keyboardScanner = new Scanner(System.in);
        System.out.println(prompt + ":\n");
        keyboardScanner.next();
    }
}
