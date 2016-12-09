package tech.notpaper.go.engine.impl;

import tech.notpaper.go.board.impl.DefaultBoard;
import tech.notpaper.go.messaging.entities.GoResponse;
import tech.notpaper.go.messaging.entities.complex.GoList;
import tech.notpaper.go.messaging.entities.complex.ListEntity;

public class DefaultEngine extends DefaultEngineProxy {
	
	private DefaultBoard board;
	private boolean closed;
	
	public DefaultEngine(int size, float komi, boolean black) {
		this.board = new DefaultBoard(size, komi);
		this.closed = false;
	}
	
	@Override
	public GoResponse quit() {
		this.closed = true;
		return new GoResponse("ACK");
	}

	@Override
	public GoResponse boardsize(GoList<ListEntity> args) {
		this.board = new DefaultBoard(Integer.parseInt(args.get(0).toString()), board.getKomi());
		return new GoResponse("ACK");
	}

	@Override
	public GoResponse clearBoard() {
		this.board.clear();
		throw new NotYetImplementedException("clear_board");
	}

	@Override
	public GoResponse komi(GoList<ListEntity> args) {
		return new GoResponse(String.valueOf(board.getKomi()));
	}

	@Override
	public GoResponse fixedHandicap(GoList<ListEntity> args) {
		throw new NotYetImplementedException("fixed_handicap");
	}

	@Override
	public GoResponse placeFreeHandicap(GoList<ListEntity> args) {
		throw new NotYetImplementedException("place_free_handicap");
	}

	@Override
	public GoResponse setFreeHandicap(GoList<ListEntity> args) {
		throw new NotYetImplementedException("set_free_handicap");
	}

	@Override
	public GoResponse play(GoList<ListEntity> args) {
		throw new UnsupportedCommandException("play");
	}

	@Override
	public GoResponse genMove(GoList<ListEntity> args) {
		throw new NotYetImplementedException("gen_move");
	}

	@Override
	public GoResponse undo() {
		throw new NotYetImplementedException("undo");
	}

	@Override
	public GoResponse timeSettings(GoList<ListEntity> args) {
		throw new NotYetImplementedException("time_settings");
	}

	@Override
	public GoResponse timeLeft(GoList<ListEntity> args) {
		throw new NotYetImplementedException("time_left");
	}

	@Override
	public GoResponse finalScore(GoList<ListEntity> args) {
		throw new NotYetImplementedException("final_score");
	}

	@Override
	public GoResponse finalStatusList(GoList<ListEntity> args) {
		throw new NotYetImplementedException("final_status_list");
	}

	@Override
	public GoResponse loadsgf(GoList<ListEntity> args) {
		throw new NotYetImplementedException("loadsgf");
	}

	@Override
	public GoResponse regGenMove(GoList<ListEntity> args) {
		throw new NotYetImplementedException("reg_gen_move");
	}

	@Override
	public GoResponse showBoard() {
		throw new NotYetImplementedException("undo");
	}
}