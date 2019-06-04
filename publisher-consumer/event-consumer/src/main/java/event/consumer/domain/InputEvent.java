package event.consumer.domain;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.UncheckedIOException;

/**
 * Event representing all inputs to the Input Disruptor
 */
public class InputEvent implements Externalizable
{

	public enum Type
	{
		QUOTE, QUOTE_STALE, QUOTE_MANUAL_OVERRIDE, QUOTE_RESET_MANUAL_OVERRIDE, SKEW, SKEW_MANUAL_OVERRIDE,
		SKEW_RESET_MANUAL_OVERRIDE, SPREAD_MANUAL_OVERRIDE, SPREAD_RESET_MANUAL_OVERRIDE, MARKET_CONDITION, TICK,
		TRADING_SIGNAL_ADAPTER_CONNECTION_LOST, SKEW_CONNECTION_LOST
	}

	//public long startOfEventNanos;
	public Type type = null;
	// public CurrencyPair currencyPair = null;
	// public ASCIICharSequence overrideSource = new ASCIICharSequence(30);
	public double bid = Double.NaN;
	public double ask = Double.NaN;
	public double skewIndex = Double.NaN;
	public double bidSkew = Double.NaN;
	public double askSkew = Double.NaN;
	public double spread = Double.NaN;
	// public MarketCondition marketCondition;
	// public TickListener tickListener;
	public long quoteId;

	@Override
	public void writeExternal(ObjectOutput out)
	{
		try
		{
			out.writeInt(type.ordinal());
			out.writeDouble(bid);
			out.writeDouble(ask);
			out.writeDouble(skewIndex);
			out.writeDouble(bidSkew);
			out.writeDouble(askSkew);
			out.writeDouble(spread);
			out.writeLong(quoteId);
		} 
		catch (IOException e)
		{
			throw new UncheckedIOException("Journaling of event failed.", e);
		}
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		throw new IOException();
	}

}