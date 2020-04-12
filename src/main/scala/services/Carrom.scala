package services

import constant.ApplicationConstant
import models._
import models.actions.StrikeActions
import writers.OutputWriter

import scala.annotation.tailrec

class Carrom(gameStatusFetcher: GameStatusFetcher, outputWriters: OutputWriter) {

  def play(players: List[Player], actionInputs: List[StrikeActions]) {
    val board = CarromBoard()
    val gameStatus = if (actionInputs.nonEmpty) {
      val (updatedPlayer, carromBoard) = performActions(actionInputs, players, board)
      gameStatusFetcher.getStatus(updatedPlayer, carromBoard)
    } else {
      GameStatus(players, ApplicationConstant.strikeOptions(), board)
    }
    outputWriters.write(gameStatus)
  }

  @tailrec
  private def performActions(actions: List[StrikeActions], players: List[Player], carromBoard: CarromBoard): (List[Player], CarromBoard) = {
    if (gameStatusFetcher.isGameOver(carromBoard)) {
      (players, carromBoard)
    } else {
      val (updatedPlayers, updatedCarromBoard) = perform(actions, players, carromBoard)
      performActions(actions.tail, updatedPlayers, updatedCarromBoard)
    }
  }

  private def perform(actions: List[StrikeActions], players: List[Player], carromBoard: CarromBoard) = {
    actions match {
      case Nil => (players, carromBoard)
      case nextAction :: _ =>
        val (updatedPlayerOnTurn, updatedCarromBoard) = nextAction.perform(findPlayerOnTurn(players), carromBoard)
        (updatesPlayers(players, updatedPlayerOnTurn), updatedCarromBoard)
    }
  }

  private def updatesPlayers(players: List[Player], updatedPlayerOnTurn: Player) = {
    val playersExcludingOnTurnPlayer = players.filterNot(_.name == updatedPlayerOnTurn.name)
    (updatedPlayerOnTurn :: playersExcludingOnTurnPlayer).map(_.updateStatus)
  }

  private def findPlayerOnTurn(players: List[Player]): Player = {
    val playerOnTurns = players.filterNot(_.isMyTurn())
    playerOnTurns.headOption.getOrElse(players.head.updateStatus)
  }

}
