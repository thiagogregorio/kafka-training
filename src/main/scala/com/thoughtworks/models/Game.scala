package com.thoughtworks.models

import java.util.UUID

import scala.util.Random

case class Game(id: String, name: String, category: String, price: Double) {
  def gameToCSVString(): String = {
    s"""${this.id};${this.name};${this.category};${this.price}\n"""
  }
}

object Game {
  def generateRandom(): Game = {
    Game(UUID.randomUUID().toString, "Game " + Random.nextInt(10), "Category " + Random.nextInt(10), Random.nextDouble())
  }
}