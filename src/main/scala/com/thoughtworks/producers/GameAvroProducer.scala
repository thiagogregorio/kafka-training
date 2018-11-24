package com.thoughtworks.producers

import java.util.Properties

import com.thoughtworks.models.Game
import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import scala.io.Source

object GameAvroProducer {

  def generateGameCSV(game: Game) = {
    game.gameToCSVString().replace("\n", "")
  }

  def generateGame(topic:String, brokers:String) = {
    val props = new Properties
    props.put("bootstrap.servers", brokers)
    props.put("client.id", "Producer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroSerializer")
    props.put("schema.registry.url", "http://ae34acbe5ed9b11e8810a0a4e9b68c10-2021023861.us-east-1.elb.amazonaws.com:8081")

    val kafkaProducer = new KafkaProducer[String,GenericData.Record](props)

    val game = Game.generateRandom()

    val parser = new Schema.Parser()
    val orderSchema = Source.fromResource("schemas/game.avsc").mkString
    val schema = parser.parse(orderSchema)
    val avroRecord = new GenericData.Record(schema)
    avroRecord.put("id", game.id)
    avroRecord.put("name", game.name)
    avroRecord.put("category", game.category)
    avroRecord.put("price", game.price)

    val record = new ProducerRecord[String, GenericData.Record](topic, game.category, avroRecord)

    kafkaProducer.send(record)
  }
}
