package com.jesusjzp.stan;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.TextView;

public class AideActivity extends Activity {
	
	TextView about, brief, title_function, function, title_legal, info_legal;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); //
        setContentView(R.layout.aide);
        
        about = (TextView)findViewById(R.id.about);
        String html = "<b>StanDroid</b><br><br>" +
        		"Version: 1.0<br>" + 
        		"Tous droits réservés<br>" +
        		"Toutes les données sont obtenues de http://www.reseau-stan.com/<br><br>" +
        		"Auteur: Zhipeng JIANG<br>" +
        		"Testeur: Fellype VEDOVATO<br>" +
        		"E-mail: jesusjzp@gmail.com<br>" +
        		"Site Web: http://jesusjzp.webege.com/<br>";
        about.setText(Html.fromHtml(html));
        
        brief = (TextView)findViewById(R.id.brief);
        String html_brief = "Toute l’information en temps réel avec " +
        		"<b>StanDroid</b> pour préparer votre voyage!<br><br>" +
        		"Rechercher et s’informer sur les horaires des " +
        		"Stan (Tram 1, Bus, minibus), et partager avec vos " +
        		"proches ces informations en les envoyant par SMS.";
        brief.setText(Html.fromHtml(html_brief));
        
        title_function = (TextView)findViewById(R.id.title_function);
        title_function.setText(Html.fromHtml("<u><b>"+"Fonctions Principales"+"</b></u>"));
        
        function = (TextView)findViewById(R.id.function);
        String html_function = "<u>Toutes vos recherches en 1 clic !</u><br><br>" +
        		"• Pour une recherche plus rapide et personnalisée : ajoutez vos favoris arrêts les plus fréquemment consultés.<br>" +
        		"• Consulter le détail de votre bus grâce à la recherche par numéro de ligne ou nom du arrêt.<br><br>" +
        		"<u>Toute l’info trafic en temps réel !</u><br><br>" +
        		"• Consulter les prochains arrivées en temps réel de votre choix en disposant de l’horaire et de la voie de départ.<br>" +
        		"• Rester connecté avec vos amis, votre famille… en partageant le détail de votre trajet ou de votre train par SMS.<br>";
        function.setText(Html.fromHtml(html_function));
        
        title_legal = (TextView)findViewById(R.id.title_legal);
        title_legal.setText(Html.fromHtml("<u><b>"+"Infos Légales"+"</b></u>"));
        
        info_legal = (TextView)findViewById(R.id.info_legal);
        String html_info_legal = "Le StanDroid s’engage à fournir un service " +
        		"de qualité auprès des utilisateurs, mais ne peut toutefois être " +
        		"tenu pour responsable de leur utilisation par ses abonnés. " +
        		"Toutes les données sont obtenues de <u>http://www.reseau-stan.com/</u><br><br>" +
        		"L’intégralité du StanDroid est protégée par les législations françaises et " +
        		"internationales relatives à la propriété intellectuelle." +
        		"Tous les droits de reproduction sont réservés. L’ensemble " +
        		"des textes, graphismes, icônes, photographies, plans, logos (…) " +
        		"et plus généralement l’ensemble des composants du StanDroid ne peuvent, " +
        		"conformément à l’article L122-4 du code de la propriété intellectuelle " +
        		"faire l’objet d’une quelconque représentation sans accord préalable du StanDroid.<br><br>";
        info_legal.setText(Html.fromHtml(html_info_legal));
	}
}
