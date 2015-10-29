package es.us.isa.ideas.repo.impl.fs;

import com.google.common.collect.ImmutableMap;

public class FSNodeIcon {
	
	public static String WORKSPACE = "workspace_icon";
	
	public static String PROJECT = "project_icon";
	
	public static String FOLDER = "folder_icon";
	
	public static String SEDL_FILE = "sedl_icon";
        
        public static String IMAGE_FILE = "image_icon";
        
        public static String DOCUMENT_FILE ="document_icon";
        
        public static String PDF_FILE ="pdf_icon";
        
        public static String SPREADSHEET_FILE ="spreadsheet_icon";

        public static String SLIDESHOW_FILE ="slideshow_icon";
	
	public static String FILE = "file_icon";
        
        public static String CSV_FILE ="csv_icon";
        
        public static String R_FILE ="r_icon";
        
        public static String BINARY_FILE = "binary_file_icon";
        
        
        private static ImmutableMap<String,String> iconsMap=ImmutableMap.<String, String>builder()
            .put("doc", DOCUMENT_FILE) 
            .put("docx", DOCUMENT_FILE) 
            .put("odt", DOCUMENT_FILE) 
            .put("xls", SPREADSHEET_FILE) 
            .put("xlsx", SPREADSHEET_FILE) 
            .put("pdf", PDF_FILE) 
            .put("ppt", SLIDESHOW_FILE) 
            .put("pptx", SLIDESHOW_FILE) 
            .put("csv", CSV_FILE)
            .put("jpg",IMAGE_FILE)
            .put("jpeg",IMAGE_FILE)
            .put("gif",IMAGE_FILE)
            .put("png",IMAGE_FILE)
            .put("r",R_FILE)
            .put("txt", FILE)            
            .build();
        
        public static String iconFor(String extension)
        {
            String icon=iconsMap.get(extension.toLowerCase());
            if(icon==null)
                icon=BINARY_FILE;
            return icon;
        }

}
