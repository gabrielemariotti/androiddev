/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.gmariotti.android.examples.googleaccount;

import android.net.Uri;
import android.provider.BaseColumns;

public class TelephonyProviderConstants {

	private TelephonyProviderConstants() {
	}

	/**
	 * Base columns for tables that contain text based SMSs.
	 */
	public interface TextBasedSmsColumns {
		/**
		 * The type of the message
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String TYPE = "type";

		public static final int MESSAGE_TYPE_ALL = 0;
		public static final int MESSAGE_TYPE_INBOX = 1;
		public static final int MESSAGE_TYPE_SENT = 2;
		public static final int MESSAGE_TYPE_DRAFT = 3;
		public static final int MESSAGE_TYPE_OUTBOX = 4;
		public static final int MESSAGE_TYPE_FAILED = 5; // for failed outgoing messages
		public static final int MESSAGE_TYPE_QUEUED = 6; // for messages to send later

		/**
		 * The thread ID of the message
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String THREAD_ID = "thread_id";

		/**
		 * The address of the other party
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String ADDRESS = "address";

		/**
		 * The person ID of the sender
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String PERSON_ID = "person";

		/**
		 * The date the message was received
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String DATE = "date";

		/**
		 * The date the message was sent
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String DATE_SENT = "date_sent";

		/**
		 * Has the message been read
		 * <P>
		 * Type: INTEGER (boolean)
		 * </P>
		 */
		public static final String READ = "read";

		/**
		 * Indicates whether this message has been seen by the user. The "seen"
		 * flag will be used to figure out whether we need to throw up a
		 * statusbar notification or not.
		 */
		public static final String SEEN = "seen";

		/**
		 * The TP-Status value for the message, or -1 if no status has been
		 * received
		 */
		public static final String STATUS = "status";

		public static final int STATUS_NONE = -1;
		public static final int STATUS_COMPLETE = 0;
		public static final int STATUS_PENDING = 32;
		public static final int STATUS_FAILED = 64;

		/**
		 * The subject of the message, if present
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String SUBJECT = "subject";

		/**
		 * The body of the message
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String BODY = "body";

		/**
		 * The id of the sender of the conversation, if present
		 * <P>
		 * Type: INTEGER (reference to item in content://contacts/people)
		 * </P>
		 */
		public static final String PERSON = "person";

		/**
		 * The protocol identifier code
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String PROTOCOL = "protocol";

		/**
		 * Whether the <code>TP-Reply-Path</code> bit was set on this message
		 * <P>
		 * Type: BOOLEAN
		 * </P>
		 */
		public static final String REPLY_PATH_PRESENT = "reply_path_present";

		/**
		 * The service center (SC) through which to send the message, if present
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String SERVICE_CENTER = "service_center";

		/**
		 * Has the message been locked?
		 * <P>
		 * Type: INTEGER (boolean)
		 * </P>
		 */
		public static final String LOCKED = "locked";

		/**
		 * Error code associated with sending or receiving this message
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String ERROR_CODE = "error_code";

		/**
		 * Meta data used externally.
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String META_DATA = "meta_data";
	}

	/**
	 * Contains all text based SMS messages.
	 */
	public static final class Sms implements BaseColumns, TextBasedSmsColumns {
		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://sms");

		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = "date DESC";

		/**
		 * Returns true iff the folder (message type) identifies an outgoing
		 * message.
		 */
		public static boolean isOutgoingFolder(int messageType) {
			return (messageType == MESSAGE_TYPE_FAILED)
					|| (messageType == MESSAGE_TYPE_OUTBOX)
					|| (messageType == MESSAGE_TYPE_SENT)
					|| (messageType == MESSAGE_TYPE_QUEUED);
		}

		/**
		 * Contains all text based SMS messages in the SMS app's inbox.
		 */
		public static final class Inbox implements BaseColumns,
				TextBasedSmsColumns {
			/**
			 * The content:// style URL for this table
			 */
			public static final Uri CONTENT_URI = Uri
					.parse("content://sms/inbox");

			/**
			 * The default sort order for this table
			 */
			public static final String DEFAULT_SORT_ORDER = "date DESC";
		}

		/**
		 * Contains all sent text based SMS messages in the SMS app's.
		 */
		public static final class Sent implements BaseColumns,
				TextBasedSmsColumns {
			/**
			 * The content:// style URL for this table
			 */
			public static final Uri CONTENT_URI = Uri
					.parse("content://sms/sent");

			/**
			 * The default sort order for this table
			 */
			public static final String DEFAULT_SORT_ORDER = "date DESC";
		}

		/**
		 * Contains all sent text based SMS messages in the SMS app's.
		 */
		public static final class Draft implements BaseColumns,
				TextBasedSmsColumns {
			/**
			 * The content:// style URL for this table
			 */
			public static final Uri CONTENT_URI = Uri
					.parse("content://sms/draft");

			/**
			 * The default sort order for this table
			 */
			public static final String DEFAULT_SORT_ORDER = "date DESC";
		}

		/**
		 * Contains all pending outgoing text based SMS messages.
		 */
		public static final class Outbox implements BaseColumns,
				TextBasedSmsColumns {
			/**
			 * The content:// style URL for this table
			 */
			public static final Uri CONTENT_URI = Uri
					.parse("content://sms/outbox");

			/**
			 * The default sort order for this table
			 */
			public static final String DEFAULT_SORT_ORDER = "date DESC";
		}

		/**
		 * Contains all sent text-based SMS messages in the SMS app's.
		 */
		public static final class Conversations implements BaseColumns,
				TextBasedSmsColumns {
			/**
			 * The content:// style URL for this table
			 */
			public static final Uri CONTENT_URI = Uri
					.parse("content://sms/conversations");

			/**
			 * The default sort order for this table
			 */
			public static final String DEFAULT_SORT_ORDER = "date DESC";

			/**
			 * The first 45 characters of the body of the message
			 * <P>
			 * Type: TEXT
			 * </P>
			 */
			public static final String SNIPPET = "snippet";

			/**
			 * The number of messages in the conversation
			 * <P>
			 * Type: INTEGER
			 * </P>
			 */
			public static final String MESSAGE_COUNT = "msg_count";
		}


	}
}
