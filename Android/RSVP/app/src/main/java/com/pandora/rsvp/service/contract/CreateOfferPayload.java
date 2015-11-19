package com.pandora.rsvp.service.contract;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class CreateOfferPayload {
    public String title;
    public int response_accept_limit;
    public String email_to;
    public long rsvp_by_time;
    public String method;
    public String invitation_body;
}
