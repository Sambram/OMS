/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package com.hybris.oms.domain.order;

import com.hybris.oms.domain.adapter.DateAdaptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlRootElement(name = "updatedSinceList")
@XmlAccessorType(XmlAccessType.NONE)
public class UpdatedSinceList<T> implements List<T>
{
	@XmlAttribute(name = "updatedDate")
	@XmlJavaTypeAdapter(DateAdaptor.class)
	private Date date;

	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<T> delegatedList;

	public UpdatedSinceList()
	{
		this.date = new Date();
		this.delegatedList = new ArrayList<>();
	}

	public UpdatedSinceList(final List<T> list)
	{
		this.date = new Date();
		this.delegatedList = list == null ? new ArrayList<T>() : new ArrayList<>(list);
	}

	public UpdatedSinceList(final Date updateDate, final List<T> list)
	{
		this(list);
		this.date = updateDate != null ? (Date) updateDate.clone() : new Date();
	}

	public List<T> getDelegatedList()
	{
		return delegatedList;
	}

	public void setDelegatedList(final List<T> delegatedList)
	{
		this.delegatedList = delegatedList;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(final Date date)
	{
		this.date = date;
	}

	@Override
	public int size()
	{
		return this.delegatedList.size();
	}

	@Override
	public boolean isEmpty()
	{
		return this.delegatedList.isEmpty();
	}

	@Override
	public boolean contains(final Object o)
	{
		return this.delegatedList.contains(o);
	}

	@Override
	public Iterator<T> iterator()
	{
		return this.delegatedList.iterator();
	}

	@Override
	public Object[] toArray()
	{
		return this.delegatedList.toArray();
	}

	@Override
	public <T1> T1[] toArray(final T1[] t1s)
	{
		return this.delegatedList.toArray(t1s);
	}

	@Override
	public boolean add(final T t)
	{
		return this.delegatedList.add(t);
	}

	@Override
	public boolean remove(final Object o)
	{
		return this.delegatedList.remove(o);
	}

	@Override
	public boolean containsAll(final Collection<?> objects)
	{
		return this.delegatedList.containsAll(objects);
	}

	@Override
	public boolean addAll(final Collection<? extends T> ts)
	{
		return this.delegatedList.addAll(ts);
	}

	@Override
	public boolean addAll(final int i, final Collection<? extends T> ts)
	{
		return this.delegatedList.addAll(i, ts);
	}

	@Override
	public boolean removeAll(final Collection<?> objects)
	{
		return this.delegatedList.removeAll(objects);
	}

	@Override
	public boolean retainAll(final Collection<?> objects)
	{
		return this.delegatedList.retainAll(objects);
	}

	@Override
	public void clear()
	{
		this.delegatedList.clear();
	}

	@Override
	public T get(final int i)
	{
		return this.delegatedList.get(i);
	}

	@Override
	public T set(final int i, final T t)
	{
		return this.delegatedList.set(i, t);
	}

	@Override
	public void add(final int i, final T t)
	{
		this.delegatedList.add(i, t);
	}

	@Override
	public T remove(final int i)
	{
		return this.delegatedList.remove(i);
	}

	@Override
	public int indexOf(final Object o)
	{
		return this.delegatedList.indexOf(o);
	}

	@Override
	public int lastIndexOf(final Object o)
	{
		return this.delegatedList.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator()
	{
		return this.delegatedList.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(final int i)
	{
		return this.delegatedList.listIterator(i);
	}

	@Override
	public List<T> subList(final int i, final int i2)
	{
		return this.delegatedList.subList(i, i2);
	}
}
