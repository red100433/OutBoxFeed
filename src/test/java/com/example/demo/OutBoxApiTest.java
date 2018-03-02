package com.example.demo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

import com.naver.fever.boot.client.exception.ClientInitializeException;
import com.naver.fever.boot.client.exception.NameServerException;
import com.naver.fever.boot.config.envronment.Profile;
import com.naver.fever.boot.model.exception.FeverThriftException;
import com.naver.fever.boot.model.field.Channel;
import com.naver.fever.boot.model.field.PageRequest;
import com.naver.fever.common.idgenerator.IdGenerator;
import com.naver.fever.feedbox.FeedboxClient;
import com.naver.fever.feedbox.model.request.DeleteContentRequest;
import com.naver.fever.feedbox.model.request.GetContentByChannelRequest;
import com.naver.fever.feedbox.model.request.GetContentRequest;
import com.naver.fever.feedbox.model.request.WriteNewContentRequest;
import com.naver.fever.feedbox.model.response.GetContentByChannelResponse;
import com.naver.fever.feedbox.model.response.GetContentResponse;

/**
 * FeedboxClientTest
 *
 * @author seongsu.kim
 */
@Slf4j
public class OutBoxApiTest {
	private static final String SERVICE_NAME = "Intern";
	private static final String CHANNEL_GROUP = "InternGroup";
	private static final String CHANNEL_ID = "testChannel5";
	private static final String USER_ID = "user1";
	private static final int LIMIT_SIZE_PER_PAGE = 30;

	private static FeedboxClient feedboxClient;

	private IdGenerator idGenerator = IdGenerator.getInstance();

	@Before
	public void before() throws NameServerException, ClientInitializeException {
		feedboxClient = new FeedboxClient(Profile.DEV);
	}

	@Test
	public void getOutboxFeeds() throws FeverThriftException {
		List<GetContentResponse> getContentResponseList = new ArrayList<GetContentResponse>();

		GetContentByChannelRequest request = new GetContentByChannelRequest("Intern",
			Channel.of("InternGroup", "testChannel5"), PageRequest.fromFirst(LIMIT_SIZE_PER_PAGE));

		GetContentByChannelResponse response = feedboxClient.getContentByChannel(request);

		response.getOutboxFeedPage().getContents().stream().forEach(a -> {
			try {
				getContentResponseList.add(getContentResponse(a.getContentId()));
			} catch (FeverThriftException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		getContentResponseList.sort((p1, p2) -> p2.getCreateDate().compareTo(p1.getCreateDate()));
	}

	@Ignore
	@Test
	public void createNewContent() throws FeverThriftException {
		String responsedContentId = writeNewContentAndGetContentId();
		log.info("contentId : {}" , responsedContentId);
	}

	private String writeNewContentAndGetContentId() throws FeverThriftException {
		String contentId = "content" + System.currentTimeMillis();
		String contentBody = "{\"name\":\"soso\", \"env\":\"test\"}";
		WriteNewContentRequest request = new WriteNewContentRequest("Intern", Channel.of("InternGroup", "testChannel1"),
			contentId, contentBody, LocalDateTime.now());

		return feedboxClient.writeNewContent(request).getContentId();
	}

	@Ignore
	@Test
	public void getContent() throws FeverThriftException {
		String contentId = writeNewContentAndGetContentId();
		GetContentResponse response = getContentResponse(contentId);
		log.info("getContentBody : {}" , response.getContentBody());
	}

	private GetContentResponse getContentResponse(String contentId) throws FeverThriftException {
		GetContentRequest request = new GetContentRequest(SERVICE_NAME, contentId);
		return feedboxClient.getContent(request);
	}

	@Ignore
	@Test
	//@Test(expected = FeverThriftException.class)
	public void deleteContent() throws FeverThriftException {
		String contentId = writeNewContentAndGetContentId();

		GetContentResponse contentResponse = getContentResponse(contentId);
		log.info("exist content : {}" , contentResponse.getContentBody());

		DeleteContentRequest request = new DeleteContentRequest("Intern", contentId);
		feedboxClient.deleteContent(request);

		// must throw "not found error"
		getContentResponse(contentId);

	}

	@Ignore
	@Test
	public void getContentByChannelTest() throws FeverThriftException {
		GetContentByChannelRequest request = new GetContentByChannelRequest(SERVICE_NAME,
			Channel.of(CHANNEL_GROUP, CHANNEL_ID), PageRequest.fromFirst(LIMIT_SIZE_PER_PAGE));
		GetContentByChannelResponse response = feedboxClient.getContentByChannel(request);

		log.info("response : {}" , response);
	}

}