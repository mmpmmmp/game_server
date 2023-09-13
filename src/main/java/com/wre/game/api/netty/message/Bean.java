package com.wre.game.api.netty.message;

import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Bean {

	private static final Logger LOGGER = LoggerFactory.getLogger(Bean.class);

	public abstract boolean write(ByteBuf buffer);

	public abstract boolean read(ByteBuf buffer);

	protected void writeInt(ByteBuf buffer, int value) {
		buffer.writeInt(value);
	}

	public void writeFloat(ByteBuf buffer, float floatValue) {
		buffer.writeFloat(floatValue);
	}

	public void writeBoolean(ByteBuf buffer, boolean value) {
		buffer.writeBoolean(value);
	}

	protected void writeString(ByteBuf buffer, String value) {
		if (value == null) {
			buffer.writeShort(0);
		} else {
			try {
				byte[] bytes = value.getBytes("UTF-8");
				buffer.writeShort(bytes.length);
				buffer.writeBytes(bytes);
			} catch (Exception ex) {
				LOGGER.error("Encode String Error:{}", ex);
			}
		}
	}

	protected void writeLong(ByteBuf buffer, long value) {
		buffer.writeLong(value);
	}

	protected void writeBean(ByteBuf buffer, Bean value) {
		if (value != null) {
			value.write(buffer);
		}
	}

	protected void writeShort(ByteBuf buffer, short value) {
		buffer.writeShort(value);
	}

	protected void writeByte(ByteBuf buffer, byte value) {
		buffer.writeByte(value);
	}

	protected void writeBytes(ByteBuf buffer, byte[] value) {
		// 为空处理
		if (value == null)
			buffer.writeShort(0);
		else {
			buffer.writeShort(value.length);
			buffer.writeBytes(value);
		}
	}

	protected int readInt(ByteBuf buffer) {
		return buffer.readInt();
	}

	public float readFloat(ByteBuf buffer) {
		return buffer.readFloat();
	}

	public boolean readBoolean(ByteBuf buffer) {
		return buffer.readBoolean();
	}

	protected String readString(ByteBuf buffer) {
		int length = buffer.readShort();
		byte[] str = new byte[length];
		buffer.readBytes(str);

		try {
			return new String(str, "UTF-8");
		} catch (Exception e) {
			// 抛出错误
			LOGGER.error("Decode String Error:" + e.getMessage());
			return new String(str);
		}
	}

	protected long readLong(ByteBuf buffer) {
		return buffer.readLong();
	}

	protected Bean readBean(ByteBuf buffer, Class<? extends Bean> clazz) {
		try {
			Bean bean = clazz.newInstance();
			bean.read(buffer);
			return bean;
		} catch (IllegalAccessException e) {
			// 抛出错误
			LOGGER.error("Decode Bean Error:" + e.getMessage());
		} catch (InstantiationException e) {
			// 抛出错误
			LOGGER.error("Decode Bean Error:" + e.getMessage());
		}
		return null;
	}

	protected short readShort(ByteBuf buffer) {
		return buffer.readShort();
	}

	protected byte readByte(ByteBuf buffer) {
		return buffer.readByte();
	}

	protected byte[] readBytes(ByteBuf buffer) {
		int length = buffer.readShort();
		if (length == 0)
			return new byte[0];
		byte[] bytes = new byte[length];
		buffer.readBytes(bytes);
		return bytes;
	}

}