package com.project.mishcma.budgetingapp.entity;

public class File {
    private String fileName;

    private File file;

    public File() {
    }

    public File(String fileName, File file) {
        this.fileName = fileName;
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "File{" +
                "fileName='" + fileName + '\'' +
                '}';
    }
}
