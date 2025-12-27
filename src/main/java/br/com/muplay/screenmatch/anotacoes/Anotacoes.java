package br.com.muplay.screenmatch.anotacoes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

public class Anotacoes {
    //Armazenar anotações de tarefas e desafios feitos durante o aprendizado. Podem ser úteis para realização do projeto screenmatch.


    //Deserializer usando Gson
    Tarefa tarefa = new Tarefa("Lavar a louça", false, "Murilo");
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    String json = gson.toJson(tarefa);
//    FileWriter arquivo = new FileWriter("tarefaGson.json");
//		arquivo.write(json);
//		arquivo.close();
//		System.out.println("\nAdicionado pelo Gson");

//    FileReader leitor = new FileReader("tarefaGson.json");
//      Tarefa tarefaLida = gson.fromJson(leitor, Tarefa.class);
//
//      System.out.println(tarefaLida);

    //Deserializer usando Jackson
    ObjectMapper objectMapper = new ObjectMapper();
    File arquivoJackson = new File("tarefaJackson.json");
//		objectMapper.writeValue(arquivoJackson, tarefa);
//		System.out.println("Adicionado pelo Jackson");

//    Tarefa lerTarefa = objectMapper.readValue(arquivoJackson, Tarefa.class);
//		System.out.println(lerTarefa);

}
