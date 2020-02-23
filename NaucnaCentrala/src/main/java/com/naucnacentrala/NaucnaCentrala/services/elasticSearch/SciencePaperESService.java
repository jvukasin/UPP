package com.naucnacentrala.NaucnaCentrala.services.elasticSearch;

import com.naucnacentrala.NaucnaCentrala.model.NaucniRad;
import com.naucnacentrala.NaucnaCentrala.model.SciencePaperES;
import com.naucnacentrala.NaucnaCentrala.repository.elasticSearch.SciencePaperESRepository;
import com.naucnacentrala.NaucnaCentrala.services.NaucniRadService;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class SciencePaperESService {

    @Autowired
    private SciencePaperESRepository sciencePaperESRepo;

    @Autowired
    private NaucniRadService naucniRadService;

    public SciencePaperES save(SciencePaperES sciencePaperES){
        return sciencePaperESRepo.save(sciencePaperES);
    }


    public SciencePaperES findOneById(String id) {
        return sciencePaperESRepo.findOneById(id);
    }

    public String parsePDF(NaucniRad naucniRad) throws UnsupportedEncodingException {
        String path = naucniRadService.getPath(naucniRad.getId());
        File pdf = new File(path);
        String text = null;
        try {
            System.out.println("*******************************************");
            System.out.println("Parsiranje PDF-a");
            System.out.println("Path: " + path);
            System.out.println("*******************************************");
            PDFParser parser = new PDFParser(new RandomAccessFile(pdf, "r"));
            parser.parse();
            text = getText(parser);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    public String getText(PDFParser parser) {
        try {
            PDFTextStripper textStripper = new PDFTextStripper();
            String text = textStripper.getText(parser.getPDDocument());
            return text;
        } catch (IOException e) {
            System.out.println("Greksa pri konvertovanju dokumenta u pdf");
        }
        return null;
    }
}
