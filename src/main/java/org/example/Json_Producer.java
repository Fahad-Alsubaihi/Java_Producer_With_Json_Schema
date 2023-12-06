package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.errors.SerializationException;

import java.io.*;
import java.util.Properties;

public class Json_Producer implements Runnable{

    private Thread thread;

    public Json_Producer() {

    }

    @Override
    public void run() {
        Properties props = new Properties();
        // get configuration from config.properties file

        InputStream is=Json_Producer.class.getClassLoader().getResourceAsStream("config.properties");
        // load a properties file for reading
        try {
            props.load(is);
        } catch (IOException e) {
            System.out.println("[ERROR] reading config.properties file: "+e.getMessage());
            throw new RuntimeException(e);
        }

        props.put("auto.register.schemas", props.getProperty("AUTO_REGISTER_SCHEMAS"));
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, props.getProperty("BOOTSTRAP_SERVERS"));

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,props.getProperty("KEY_SERIALIZER"));
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,props.getProperty("VALUE_SERIALIZER"));


        props.put("schema.registry.url", props.getProperty("SCHEMA_REGISTRY_URL"));


            try(Producer<String, jsondata> producer = new KafkaProducer<String,jsondata>(props)){



                    try {
                        // Specify the path to your JSON file

                        // Create ObjectMapper instance
                        ObjectMapper objectMapper = new ObjectMapper();


                        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/org/example/sample.txt"))) {

                            // Read each line (containing a JSON object) from the file
                            String line;
                            while ((line = reader.readLine()) != null) {

                                // Parse each line into a JsonNode object
                                JsonNode jsonNode = objectMapper.readTree(line);

                                // Now you can work with each individual JSON object
                                String name = jsonNode.get("name").asText();
                                int id = jsonNode.get("id").asInt();
                                String email = jsonNode.get("email").asText();


                                ProducerRecord<String, jsondata> record = new ProducerRecord<String,jsondata>(props.getProperty("TOPIC"),null, new jsondata(id,name,email));
                                producer.send(record, new Callback() {
                                    @Override
                                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                                        if (exception == null) {

                                            System.out.println("Message sent successfully to partition " + metadata.partition()
                                                    + " at offset " + metadata.offset()+ " with key "+record.key()+ " with value "+record.value());

                                        } else {
                                            System.err.println("Failed to send message: " + exception.getMessage());
                                        }
                                    }
                                });
                                Thread.sleep(1000);
                            }

                        } catch (SerializationException e) {
                            System.out.println("[ERROR] JSON SERIALIZER: "+e.getMessage());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


            }catch (Exception e){
                System.out.println("[ERROR] Create Kafka Producer: "+e.getMessage());
            }


    }

    public void start () {
        System.out.println("Starting Producer");
        if (thread == null) {
            thread = new Thread (this);
            thread.start ();
        }
    }
}
