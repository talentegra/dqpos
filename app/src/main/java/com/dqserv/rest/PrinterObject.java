package com.dqserv.rest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 2/12/2018.
 */

public class PrinterObject {

    @SerializedName("printers")
    private List<Printers> printers;

    public static class Printers {

        public Printers() {
        }

        @SerializedName("printer_id")
        private String printerId;

        @SerializedName("title")
        private String title;

        @SerializedName("printer_type")
        private String printerType;

        @SerializedName("profile")
        private String profile;

        @SerializedName("char_per_line")
        private String charPerLine;

        @SerializedName("path")
        private String path;

        @SerializedName("ip_address")
        private String ipAddress;

        @SerializedName("port")
        private String port;

        public String getPrinterId() {
            return printerId;
        }

        public void setPrinterId(String printerId) {
            this.printerId = printerId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrinterType() {
            return printerType;
        }

        public void setPrinterType(String printerType) {
            this.printerType = printerType;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getCharPerLine() {
            return charPerLine;
        }

        public void setCharPerLine(String charPerLine) {
            this.charPerLine = charPerLine;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }
    }

    public void setPrinters(List<Printers> printers) {
        this.printers = printers;
    }

    public List<Printers> getPrinters() {
        return printers;
    }
}
