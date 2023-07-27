package zxf.java.memory.pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import zxf.java.memory.util.DebugUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PdfboxTests {
    public static void main(String[] args) throws IOException, InterruptedException {
        loadPdf();
        DebugUtils.printMemInfoFromRuntime("main.1");
//        System.gc();
//        DebugUtils.printMemInfoFromRuntime("main.2");
//        loadPdf();
//        DebugUtils.printMemInfoFromRuntime("main.3");
//        System.gc();
//        DebugUtils.printMemInfoFromRuntime("main.4");
//        loadPdf();
//        DebugUtils.printMemInfoFromRuntime("main.5");
//        System.gc();
//        DebugUtils.printMemInfoFromRuntime("main.6");
    }

    private static void loadPdf() throws IOException, InterruptedException {
        System.gc();
        DebugUtils.printMemInfoFromRuntime("loadPdf.start");
        //DebugUtils.callJmap("loadPdf.start");
        List<BufferedImage> results = new ArrayList<>();
        try (PDDocument pdfDocument = PDDocument.load(Paths.get("./testpdf/developer-mozilla-org-CORS-en.pdf").toFile())) {
            DebugUtils.printMemInfoFromRuntime("loadPdf.after.load");
            //DebugUtils.callJmap("loadPdf.after.load");
            PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
            for (int pageIndex = 0; pageIndex < pdfDocument.getNumberOfPages(); pageIndex++){
                BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, 200);
                results.add(image);
            }
        }
        DebugUtils.printMemInfoFromRuntime("loadPdf.end");
        //DebugUtils.callJmap("loadPdf.end");
    }
}
