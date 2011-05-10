/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 Copyright (c) 2010, Janrain, Inc.
 
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without modification,
 are permitted provided that the following conditions are met:
 
 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer. 
 * Redistributions in binary form must reproduce the above copyright notice, 
 this list of conditions and the following disclaimer in the documentation and/or
 other materials provided with the distribution. 
 * Neither the name of the Janrain, Inc. nor the names of its
 contributors may be used to endorse or promote products derived from this
 software without specific prior written permission.
 
 
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
*/
package com.janrain.android.engage;

import com.janrain.android.engage.net.async.HttpResponseHeaders;
import com.janrain.android.engage.types.JRActivityObject;
import com.janrain.android.engage.types.JRDictionary;

/**
 * @brief
 * The JREngageDelegate interface is implemented by an object in order to receive notifications
 * when a user authenticates, or publishes an activity to their social networks.
 *
 * The methods of this interface are invoked upon the success or failure of the Engage for Android
 * Activities. They provide a conduit for the authenticated user's profile data, and if server-side
 * authentication is configured, the data payload returned by your
 * server's token URL.
 **/

public interface JREngageDelegate {
	
/**
 * \name Configuration
 * Messages sent by JREngage during dialog launch/configuration
 **/
/*@{*/

	/**
	 * Notifies the delegate when the application invokes the display of a library Activity, but the
     * Activity fails to start. May occur if the library failed to connect to the Engage server, or
     * if the JRActivityObject was null, etc.
	 *  
	 * @param error
	 *   the error that occurred during configuration
	 *
	 * @note
	 * This message is only sent if your application tries to show a JREngage dialog, and not
	 * necessarily when the error occurred. For example, if the error occurred during the library's
	 * configuration with the Engage server, it will not be sent through this interface until the
     * application attempts to display a library Activity.
     *
     * The raison d'etre for this delayed delegate delivery is to allow for the possibility that an
     * application may speculatively configure the library, but never actually invoke any library
     * Activies.  In that case, no error is delivered to the application.
	 **/
    void jrEngageDialogDidFailToShowWithError(JREngageError error);
/*@}*/
    

/**
 * \name Authentication
 * Messages sent  by JREngage during authentication
 **/
/*@{*/

    /**
     * Notifies the delegate that authorization was canceled for any reason other than an error.
     * For example:
     * the user pressed the back button, the cancelAuthentication method was called, or
     * configuration of the library timed out.
     **/
    void jrAuthenticationDidNotComplete();
    
    /**
     * \anchor authDidSucceed
     *
     * Notifies the delegate that the user has successfully authenticated with the given provider,
     * passing to the delegate a \c JRDictionary object with the user's profile data.
     *
     * @param auth_info
     *   a \c JRDictionary of fields containing all the information that Janrain Engage knows about
     *   the user logging into your application.  Includes the field "profile" which contains the
     *   user's  profile information.
     *
     * @param provider
     *   the name of the provider on which the user authenticated.  For a list of possible strings,
     *   please see the \ref basicProviders "List of Providers"
     *
     * @note
     *   The structure of the auth_info JRDictionary (represented here in JSON) will be like the
     *   following:
     * \code
     "auth_info":
     {
       "profile":
       {
         "displayName": "brian",
         "preferredUsername": "brian",
         "url": "http:\/\/brian.myopenid.com\/",
         "providerName": "Other",
         "identifier": "http:\/\/brian.myopenid.com\/"
       }
     }
     * \endcode
     *
     * \sa For a full description of the dictionary and its fields,
     * please see the <a href="https://rpxnow.com/docs#api_auth_info_response">auth_info 
     * response</a> section of the Janrain Engage API documentation.
     **/
    void jrAuthenticationDidSucceedForUser(JRDictionary auth_info, String provider);

    /**
     * Notifies the delegate when authentication has failed and could not be recovered by the
     * library.
     *
     * @param error
     *   the error that occurred during authentication
     *
     * @param provider
     *   the name of the provider on which the user tried to authenticate.  For a list of possible
     *   strings, please see the \ref basicProviders "List of Providers"
     *
     * \note
     * This message is not sent if authentication was canceled.  To be notified of a canceled 
     * authentication, see jrAuthenticationDidNotComplete().
     **/
    void jrAuthenticationDidFailWithError(JREngageError error, String provider);

    /**
     * Notifies the delegate after the library has successfully posted the Engage auth_info token to
     * your server application's token URL, passing to the delegate the body of the HTTP response
     * from the token URL.
     *
     * @param tokenUrl
     *   the URL on the server where the token was posted and server-side authentication was
     *   completed
     *
     * @param tokenUrlPayload
     *   the response from the server
     *
     * @param provider
     *   the name of the provider on which the user authenticated.  For a list of possible strings,
     *   please see the \ref basicProviders "List of Providers"
     * 
     * \warning This function may be deprecated in the future.
     *
     * \sa \ref tokenUrlReached "void jrAuthenticationDidReachTokenUrl(String tokenUrl, String tokenUrlPayload, String provider)"
     **/
    void jrAuthenticationDidReachTokenUrl(String tokenUrl, String tokenUrlPayload, String provider);

    /**
     * \anchor tokenUrlReached
     *
     * Notifies the delegate after the library has successfully posted the Engage auth_info token to
     * your server application's token URL, passing to the delegate the body and headers of the HTTP
     * response from the token URL.
     *
     * @param tokenUrl
     *   the URL on the server where the token was posted and server-side authentication was completed
     *
     * @param response
     *   the response headers returned from the server
     *
     * @param tokenUrlPayload
     *   the response from the server
     *
     * @param provider
     *   the name of the provider on which the user authenticated.  For a list of possible strings,
     *   please see the \ref basicProviders "List of Providers"
     **/
    void jrAuthenticationDidReachTokenUrl(String tokenUrl, HttpResponseHeaders response,
                                          String tokenUrlPayload, String provider);

    /**
     * Notifies the delegate when the call to the token URL has failed.
     *
     * @param tokenUrl
     *   the URL on the server where the token was posted and server-side authentication was
     *   completed
     *
     * @param error
     *   the error that occurred during server-side authentication
     *
     * @param provider
     *   the name of the provider on which the user authenticated.  For a list of possible strings,
     *   please see the \ref basicProviders "List of Providers"
     **/
    void jrAuthenticationCallToTokenUrlDidFail(String tokenUrl, JREngageError error, String provider);
/*@}*/

/**
 * \name SocialPublishing
 * Messages sent by JREngage during social publishing
 **/
/*@{*/

    /**
     * Notifies the delegate if social publishing was canceled for any reason other than an error.
     * For example, the user presses the back button, any class (e.g., the JREngage delegate) calls
     * the cancelPublishing method, or if configuration of the library times out.
     **/
    void jrSocialDidNotCompletePublishing();
    
    /**
     * Notifies the delegate after the social publishing dialog is closed (e.g., the user presses
     * the back button) and publishing is complete. You may receive multiple \ref didPublish
     * "void jrSocialDidPublishJRActivity(JRActivityObject activity, String provider)"
     * messages before the dialog is closed and publishing is complete.
     **/
    void jrSocialDidCompletePublishing();

    /**
     * \anchor didPublish
     * Notifies the delegate after the user successfully shares an activity on the given provider.
     *
     * @param activity
     *   the shared activity
     *
     * @param provider
     *   the name of the provider on which the user published the activity.  For a list of possible
     *   strings, please see the \ref socialProviders "List of Social Providers"
     **/
    void jrSocialDidPublishJRActivity(JRActivityObject activity, String provider);
    
    /**
     * Notifies the delegate when publishing an activity failed and could not be recovered by the
     * library.
     *
     * @param activity
     *   the activity the user was trying to share
     *
     * @param error
     *   the error that occurred during publishing
     *
     * @param provider
     *   the name of the provider on which the user attempted to publish the activity.  For a list
     *   of possible strings, please see the \ref socialProviders "List of Social Providers"
     **/
    void jrSocialPublishJRActivityDidFail(JRActivityObject activity, JREngageError error, String provider);
/*@}*/
}
