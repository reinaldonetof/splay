package studio.rcs.com.splayv2;

import android.app.Fragment;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroMain extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_intro_main);

        addSlide(AppIntroFragment.newInstance("S&Play", "Olá, seja bem vindo(a) ao S&Play, aplicativo destinado a legendar suas músicas favoritas com a sua ajuda!",
                R.mipmap.ic_launcheroficial, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("1- Passo", "É bom que você já tenha o vídeo que deseja legendar salvo no seu celular, caso não tenha baixe nos formatos .mp4, .3gp ou .wmv\nCom o vídeo no celular, você irá selecionar a opção. ",
                R.drawable.imagem1, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("2- Passo", "Caso seja a primeira vez que utilizas o aplicativo a memória interna do celular estará oculta, siga os passos apresentados para poder pesquisar tanto no cartão de memória como na memória interna.\n\nEm seguida,    procure pelo seu vídeo!",
                R.drawable.imagem2, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("3- Passo", "Após adicionar o vídeo, você irá adicionar a legenda, através dos passos demonstrados na imagem!",
                R.drawable.imagem3, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("4- Passo", "Aqui é a área para adicionar a legenda através do botão 'Cole Aqui'.\n Este é passo é muito importante, pois você necessita ter a letra 'Copiada' como na figura 2! \n Para procurar pela letra, você pode procurar em sites como letras.mus.br, ou vagalume.com.br, ou genius.com",
                R.drawable.imagem4, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("5- Passo", "Após a letra copiada, podemos clicar no botão 'Cole Aqui'\n A letra copiada aparece na caixa de texto como mostra a figura 1, porém alguns trechos não fazem parte da letra, partes como [Choru:..] ou espaços, esses podem ser deletados como mostrado na figura 2!",
                R.drawable.imagem5, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("6- Passo", "Voltando à tela inicial o video escolhido e a Legenda inserida devem estar com check verde, ativando o botão para ir para a ação, bastando clicar no botão Inicio, o vídeo será iniciado após 3 segundos!",
                R.drawable.imagem6, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("7- Passo", "Agora está na hora da ação!\n\n Neste momento existem 2 botões, 'Inicio'- representa o início de um verso e 'Fim'-representa o final do verso! \n Obs.: Deve-se tomar cuidado quando clicar no Fim de um verso e Inicio do outro, para algumas músicas muito rápidas!",
                R.drawable.imagem7, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("8- Passo", "Pronto, o arquivo .srt foi gerado após o último verso! E você irá encontra na memoria interna do celular, na pasta Splay/Legendas/",
                R.drawable.imagem8, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)));
    }


    @Override
    public void onDonePressed() {
        super.onDonePressed();
        Intent intent= new Intent(getApplicationContext(),TelaHome.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onSkipPressed() {
        super.onSkipPressed();
        Intent intent= new Intent(getApplicationContext(),TelaHome.class);
        startActivity(intent);
        finish();
    }
}
