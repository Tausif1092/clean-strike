package models.actions

import models.{CarromBoard, Player}

class StrikersStrike extends StrikeActions {

  override def perform(player: Player, carromBoard: CarromBoard): (Player, CarromBoard) = {
    (player.addFoul.losePoint.addSuccessiveFailTurnCount, carromBoard)
  }
}
