package id.belisawit.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.webkit.*;
import android.content.Context;
import android.print.PrintManager;
import android.print.PrintAttributes;
import android.text.InputType;
import android.widget.EditText;

public class MainActivity extends Activity {
  WebView web;

  @Override public void onCreate(Bundle b) {
    super.onCreate(b);
    web = new WebView(this);
    web.getSettings().setJavaScriptEnabled(true);
    web.getSettings().setDomStorageEnabled(true);
    web.setWebViewClient(new WebViewClient());
    web.setWebChromeClient(new WebChromeClient() {
      @Override public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        new AlertDialog.Builder(MainActivity.this)
          .setMessage(message)
          .setPositiveButton("OK", (dialog, which) -> result.confirm())
          .setOnCancelListener(dialog -> result.cancel())
          .show();
        return true;
      }

      @Override public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        new AlertDialog.Builder(MainActivity.this)
          .setMessage(message)
          .setPositiveButton("Ya", (dialog, which) -> result.confirm())
          .setNegativeButton("Batal", (dialog, which) -> result.cancel())
          .setOnCancelListener(dialog -> result.cancel())
          .show();
        return true;
      }

      @Override public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        EditText input = new EditText(MainActivity.this);
        input.setText(defaultValue == null ? "" : defaultValue);
        input.setSingleLine(true);
        new AlertDialog.Builder(MainActivity.this)
          .setTitle(message)
          .setView(input)
          .setPositiveButton("Simpan", (dialog, which) -> result.confirm(input.getText().toString()))
          .setNegativeButton("Batal", (dialog, which) -> result.cancel())
          .setOnCancelListener(dialog -> result.cancel())
          .show();
        return true;
      }
    });
    web.addJavascriptInterface(new PrintBridge(), "AndroidPrint");
    setContentView(web);
    web.loadUrl("file:///android_asset/index.html");
  }

  class PrintBridge {
    @JavascriptInterface public void print() {
      runOnUiThread(() -> printWebView(web, "AZLAN-PALMA-LESTARI-Laporan"));
    }
    @JavascriptInterface public void printHtml(String html) {
      runOnUiThread(() -> {
        WebView w = new WebView(MainActivity.this);
        w.setWebViewClient(new WebViewClient() {
          @Override public void onPageFinished(WebView v, String u) {
            printWebView(v, "AZLAN-PALMA-LESTARI-Nota");
          }
        });
        w.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
      });
    }
  }

  void printWebView(WebView v, String name) {
    PrintManager pm = (PrintManager)getSystemService(Context.PRINT_SERVICE);
    if (pm != null) pm.print(name, v.createPrintDocumentAdapter(name), new PrintAttributes.Builder().build());
  }

  @Override public void onBackPressed() {
    if (web != null && web.canGoBack()) web.goBack();
    else super.onBackPressed();
  }
}
