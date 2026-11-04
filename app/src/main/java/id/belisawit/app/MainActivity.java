package id.belisawit.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.*;
import android.content.Context;
import android.print.PrintManager;
import android.print.PrintAttributes;
import android.view.ViewGroup;

public class MainActivity extends Activity {
  WebView web;
  @Override public void onCreate(Bundle b) { super.onCreate(b); web=new WebView(this); web.getSettings().setJavaScriptEnabled(true); web.getSettings().setDomStorageEnabled(true); web.setWebViewClient(new WebViewClient()); web.addJavascriptInterface(new PrintBridge(), "AndroidPrint"); setContentView(web); web.loadUrl("file:///android_asset/index.html"); }
  class PrintBridge {
    @JavascriptInterface public void print() { runOnUiThread(() -> printWebView(web, "BELI-SAWIT-Laporan")); }
    @JavascriptInterface public void printHtml(String html) { runOnUiThread(() -> { WebView w=new WebView(MainActivity.this); w.setWebViewClient(new WebViewClient(){ @Override public void onPageFinished(WebView v,String u){ printWebView(v,"BELI-SAWIT-Nota"); }}); w.loadDataWithBaseURL(null,html,"text/html","UTF-8",null); }); }
  }
  void printWebView(WebView v,String name) { PrintManager pm=(PrintManager)getSystemService(Context.PRINT_SERVICE); if(pm!=null) pm.print(name,v.createPrintDocumentAdapter(name),new PrintAttributes.Builder().build()); }
  @Override public void onBackPressed(){ if(web!=null && web.canGoBack()) web.goBack(); else super.onBackPressed(); }
}
