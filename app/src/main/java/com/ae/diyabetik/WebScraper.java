package com.ae.diyabetik;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class WebScraper {

    private ArrayList<String> foodNamesArrayList = new ArrayList<>();
    String foodName = " ";
    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            int pages = 2;

            Document foodNamesDocument = null;
            try {
                for (int i = 1; i <= pages; i++) {
                    String urlNames = "https://www.diyetkolik.com/kac-kalori/arama/" + foodName + "?p=" + i;
                    foodNamesDocument = Jsoup.connect(urlNames).get();
                    Elements foodNameElements = foodNamesDocument.select("a.kkfs16.maviLink.d-block");

                    if (foodNameElements.isEmpty()) {
                        break;
                    }
                    for (Element element : foodNameElements) {
                        foodNamesArrayList.add(element.text());
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });
    private Thread thread1 = new Thread(new Runnable() {
        @Override
        public void run() {
            String urlFood = "https://www.diyetkolik.com/kac-kalori/tavuk-sote";

            Document doc = null;
            try {
                doc = Jsoup.connect(urlFood).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Element carbElement = doc.selectXpath("//*[@id=\"solAnaSutun\"]/div[2]/div[4]/div[1]/div[1]/table/tbody/tr[1]/td[1]/span").get(0);
            Element proteinElement = doc.selectXpath("//*[@id=\"solAnaSutun\"]/div[2]/div[4]/div[1]/div[1]/table/tbody/tr[2]/td[1]/span").get(0);
            Element oilElement = doc.selectXpath("//*[@id=\"solAnaSutun\"]/div[2]/div[4]/div[1]/div[1]/table/tbody/tr[3]/td[1]/span").get(0);

            float carbText = Float.parseFloat(carbElement.text());
            float proteinText = Float.parseFloat(proteinElement.text());
            float oilText = Float.parseFloat(oilElement.text());

            float calorieFor100gram = carbText * 4 + proteinText * 4 + oilText * 9;
        }
    });

    public ArrayList<String> getFoodNamesArrayList() {
        return foodNamesArrayList;
    }

    public void setFoodNamesArrayList(ArrayList<String> foodNamesArrayList) {
        this.foodNamesArrayList = foodNamesArrayList;
    }

    public Thread getThread() {
        return thread;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
