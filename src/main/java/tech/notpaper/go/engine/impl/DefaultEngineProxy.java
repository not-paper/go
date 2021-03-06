package tech.notpaper.go.engine.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.exception.ExceptionUtils;

import tech.notpaper.go.engine.EnhancedEngine;
import tech.notpaper.go.messaging.entities.GoCommand;
import tech.notpaper.go.messaging.entities.GoResponse;
import tech.notpaper.go.messaging.entities.complex.GoList;
import tech.notpaper.go.messaging.entities.complex.ListEntity;
import tech.notpaper.go.messaging.entities.simple.GoString;
import tech.notpaper.go.messaging.entities.simple.SimpleEntity;

public abstract class DefaultEngineProxy implements EnhancedEngine {
	
	private static List<String> supportedCommands;
	private static boolean debug = true;
	
	static {
		supportedCommands = new LinkedList<String>();
		supportedCommands.add("protocol_version");
		supportedCommands.add("name");
		supportedCommands.add("version");
		supportedCommands.add("list_commands");
		supportedCommands.add("quit");
		supportedCommands.add("boardsize");
		supportedCommands.add("clear_board");
		supportedCommands.add("komi");
		supportedCommands.add("play");
		supportedCommands.add("genmove");
		supportedCommands.add("undo");
		supportedCommands.add("showboard");
	}

	protected boolean closed;

	@Override
	public String accept(String s) {
		
		//first scrub the command that you received
		s = scrubCommand(s);
		
		//then construct a GoCommand from it
		GoCommand goCommand = new GoCommand(s);
		
		//fetch the id from the command to add it to the response
		SimpleEntity id = goCommand.getId();
		if (id == null || id.toString().trim().isEmpty()) {
			id = new GoString("");
		}
		
		//get the command name portion of the GoCommand
		SimpleEntity command = goCommand.getCommand();
		
		//get the args for the command
		GoList<ListEntity> args = goCommand.getArgs();
		
		//printing the accepted command
		if(debug) {
			System.out.print(goCommand);
		}
		
		//process the command
		try {
			switch(command.toString()) {
			case "protocol_version":
				return this.protocolVersion().setId(id).toString();
			case "name":
				return this.name().setId(id).toString();
			case "version":
				return this.version().setId(id).toString();
			case "known_command":
				return this.knownCommand(args).setId(id).toString();
			case "list_commands":
				return this.listCommands().setId(id).toString();
			case "quit":
				return this.quit().setId(id).toString();
			case "boardsize":
				return this.boardsize(args).setId(id).toString();
			case "clear_board":
				return this.clearBoard().setId(id).toString();
			case "komi":
				return this.komi(args).setId(id).toString();
			case "fixed_handicap":
				return this.fixedHandicap(args).setId(id).toString();
			case "place_free_handicap":
				return this.placeFreeHandicap(args).setId(id).toString();
			case "set_free_handicap":
				return this.setFreeHandicap(args).setId(id).toString();
			case "play":
				return this.play(args).setId(id).toString();
			case "genmove":
				return this.genMove(args).setId(id).toString();
			case "undo":
				return this.undo().setId(id).toString();
			case "time_settings":
				return this.timeSettings(args).setId(id).toString();
			case "time_left":
				return this.timeLeft(args).setId(id).toString();
			case "final_score":
				return this.finalScore(args).setId(id).toString();
			case "final_status_list":
				return this.finalStatusList(args).setId(id).toString();
			case "loadsgf":
				return this.loadsgf(args).setId(id).toString();
			case "reg_gen_move":
				return this.regGenMove(args).setId(id).toString();
			case "showboard":
				return this.showBoard().setId(id).toString();
				default:
					throw new IllegalArgumentException("Failed to parse message received by engine [" + s + "]");
			}
		} catch (Exception e) {
			return renderException(e).setId(id).toString();
		}
	}
	
	private static final Pattern ILLEGAL_PATTERN = Pattern.compile("[^-_. \\tA-Za-z0-9]");
	private static final Pattern COMMENT_PATTERN = Pattern.compile("^\\s+#.*");
	private static final Pattern HT_PATTERN = Pattern.compile("\\t");
	private static final Pattern BLANK_LINE_PATTERN = Pattern.compile("^\\s+\\n$");
	private String scrubCommand(String s) {
		s = s.replaceAll("\r", "");
		s = ILLEGAL_PATTERN.matcher(s).replaceAll("");
		s = COMMENT_PATTERN.matcher(s).replaceAll("");
		s = HT_PATTERN.matcher(s).replaceAll("  ");
		s = BLANK_LINE_PATTERN.matcher(s).replaceAll("");
		return s;
	}
	
	private GoResponse renderException(Throwable e) {
		Throwable re = new RuntimeException("An exception was encountered when parsing the command\n" +
				"\tProtocol" + toGoString(this.protocolVersion().toString()).toString() + ", " +
				" Name" + toGoString(this.name().toString()).toString() + ", " +
				" Version" + toGoString(this.version().toString()).toString() + "\n" +
				"Caused by: " + toGoString(e.getMessage()).toString() + "\t", e);
		GoResponse response = new GoResponse(re.getMessage() + "\n" + ExceptionUtils.getStackTrace(re));
		response.setStatus(GoResponse.Status.FAIL);
		return response;
	}
	
	private static GoString toGoString(String s) {
		return s==null || s.trim().isEmpty() ? new GoString("") : new GoString(s.trim());
	}
	
	@Override
	public GoResponse protocolVersion() {
		return new GoResponse("2");
	}
	
	@Override
	public GoResponse name() {
		return new GoResponse("tech.notpaper.go");
	}
	
	@Override
	public GoResponse version() {
		return new GoResponse("0.0.1-SNAPSHOT");
	}

	@Override
	public GoResponse knownCommand(GoList<ListEntity> args) {
		throw new UnsupportedCommandException("known_command");
	}

	@Override
	public GoResponse listCommands() {
		GoResponse response = new GoResponse();
		for (String c : supportedCommands) {
			response.addLine(c);
		}
		return response;
	}

	@Override
	public GoResponse quit() {
		this.closed = true;
		return new GoResponse();
	}

	

	public final class UnsupportedCommandException extends UnsupportedOperationException {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 9044760197558384736L;

		public UnsupportedCommandException(String message, Throwable cause) {
			super(message, cause);
		}
		
		public UnsupportedCommandException(String command) {
			super("unsupported command [" + command + "]");
		}
	}
	
	public final class NotYetImplementedException extends UnsupportedOperationException {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3464614784137968017L;

		public NotYetImplementedException(String message, Throwable cause) {
			super(message, cause);
		}
		
		public NotYetImplementedException(String command) {
			super("unsupported command [" + command + "] is not yet implemented");
		}
	}

	@Override
	public boolean isClosed() {
		return this.closed;
	}
}
