/**
* Copyright (C) 2009  Leo Nomdedeu, David Olmos
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*********************************************************************
*
* Authors: Leo Nomdedeu, David Olmos
* Release: 0.1pre_alfa
* Changelog:
* 		0.1pre_alfa: Initial release
*********************************************************************
*/

package es.uji.robot.player.proxy;

public class QueueOverflowException extends ClientException {

	private static final long serialVersionUID = 3115108500678715271L;

	public QueueOverflowException() {
		super();
	}

	public QueueOverflowException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public QueueOverflowException(String arg0) {
		super(arg0);
	}

	public QueueOverflowException(Throwable arg0) {
		super(arg0);
	}

}
