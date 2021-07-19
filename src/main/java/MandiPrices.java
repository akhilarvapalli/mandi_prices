import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MandiPrices {
    private static Map<String,Integer> mp_commodity=new HashMap<String, Integer>() ;
    private static Map<String,Integer> mp_state=new HashMap<String,Integer>();
    public static void writeToCSV(String str){
        String[] dat = str.split(";");
        try {
            File file = new File("./"+dat[0]);
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);
            ArrayList<String[]> m=new ArrayList<String[]>();
            m.add(dat);
            writer.writeAll(m);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int getId(String str) {
       if(str.charAt(0)=='/') str = str.substring(1);
       return Integer.parseInt(str);
    }
    private static String getName(String val,boolean state) {
        int l=state?4:0;
        String base="https://www.commodityonline.com/mandiprices/";
        val=val.substring(base.length()+l);
        return val.substring(0,val.indexOf('/'));
    }
    public static void get_state_and_commodity()throws IOException{
        Document doc = Jsoup.connect("https://www.commodityonline.com/mandiprices").get();
        Elements links = doc.getElementsByClass("form-control list_plus");
        String str1="";int id=0;String name = "";
        boolean state=false;
        for (Element link : links) {
            for (Element k : link.getElementsByTag("option")) {
                String lin=k.val().toString();
                if(str1!=""&&!state){
                    id=getId(lin.substring(lin.length()-7,lin.length()-4));
                    name=getName(k.val(),state);
                    mp_commodity.put(k.val(),id);
                }
                else if(str1!=""&&state){
                    id=getId(lin.substring(lin.length()-2));
                    name=getName(k.val(),state);
                    mp_state.put(k.val(),id);
                }
                str1+=k.text()+";";
            }
            writeToCSV(str1);str1="";state=true;
        }
    }
    public static int  get_list_url(String url,CSVWriter writer)throws IOException{
        Document doc = Jsoup.connect(url).get();
        System.out.println(url);
        Elements items =doc.getElementsByClass("mob_p_12");
        int n=items.size();
        if(n>0){
            for(Element element : items){
                ArrayList<String> str=new ArrayList<String>();
                String com=element.getElementsByClass("mob_p_02").text();
                String[] str2=element.getElementsByClass("mob_p_06").text().split(" : ");
                String[] str3 = element.getElementsByClass("mob_p_09").text().split(" /");
                str.add(com);
                str.add(str2[0].substring(2));
                str.add(str2[1]);
                str.add(str2[2]);
                str.add(str3[0].substring(2));
                str.add(str3[1].substring(1));
                System.out.println(str);
                String[] s= str.toArray(new String[0]);
                writer.writeNext(s);
            }
        }
        return n;
    }
    public static void get_list_updated() throws IOException {
        File file = new File("./updatedlist");
        FileWriter outputfile = new FileWriter(file);
        CSVWriter writer = new CSVWriter(outputfile);
        int n=1;
        int count=0;
        while(n>0){
            n=get_list_url("https://www.commodityonline.com/mandiprices/"+count,writer);
            count+=n;
            System.out.println(count);
        }
        writer.close();
    }
    public static void main(String[] args)throws IOException{
//        get_state_and_commodity();

        get_list_updated();
    }
}