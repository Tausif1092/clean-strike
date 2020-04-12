package models.actions

import models.{CarromBoard, Player}

class Strike extends StrikeActions {

  override def perform(player: Player, carromBoard: CarromBoard): (Player, CarromBoard) = {
    (player.addPoint.removeSuccessiveFailTurn, carromBoard.pocketBlackCoin())
  }
}
