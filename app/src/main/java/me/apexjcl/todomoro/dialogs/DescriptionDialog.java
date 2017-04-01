package me.apexjcl.todomoro.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import butterknife.ButterKnife;
import me.apexjcl.todomoro.R;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * Created by apex on 31/03/17.
 */
public class DescriptionDialog extends DialogFragment {

    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflate = inflater.inflate(R.layout.dialog_description, null);
        ButterKnife.bind(inflate, getActivity());
        builder.setView(inflate);
        WebView webView = (WebView) inflate.findViewById(R.id.webView);
        webView.loadDataWithBaseURL("", renderer.render(parser.parse(content)), "text/html", "UTF-8", "");
        return builder.create();
    }
}
