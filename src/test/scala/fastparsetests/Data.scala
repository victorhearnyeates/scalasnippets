package fastparsetests

object Data {

  val cells =
    """
      |<td>1</td>
      | <td>2</td>
      |  <td>3</td>
      |    <td>4</td>
      |
    """

  val rows =
    """
      |<tr>
      |  <td>1</td>
      |</tr>
      |<tr>
      |  <td>2</td>
      |</tr>
      |<tr>
      |  <td>3</td>
      |</tr>
    """

  val html =
    """
      |<!DOCTYPE html>
      |<!--[if IE 7]><html lang="en" class="no-js ie7 lte-ie9 lte-ie8 lte-ie7 ie"><![endif]-->
      |<!--[if IE 8]><html lang="en" class="no-js ie8 lte-ie9 lte-ie8 ie"><![endif]-->
      |<!--[if IE 9]><html lang="en" class="no-js ie9 lte-ie9 ie"><![endif]-->
      |<!--[if (gt IE 9)|!(IE)]><!-->
      |<html lang="en" class="no-js">
      |  <!--<![endif]-->
      |  <head>
      |    <meta charset="utf-8">
      |      <title>English and Welsh Membership Check - Member Details | Swim England</title>
      |      <meta name="robots" content="noindex, nofollow">
      |        <meta name="apple-mobile-web-app-status-bar-style" content="black" />
      |        <meta name="apple-mobile-web-app-capable" content="yes" />
      |        <meta name="format-detection" content="telephone=no" />
      |        <meta id="viewport" name="viewport" content="width=800">
      |          <link rel="stylesheet" type="text/css" media="all" href="/css/british_swimming_rb.css?version=2017.04.04.3" />
      |          <link rel="stylesheet" type="text/css" media="all" href="/css/custom/rankings.css?version=2017.04.04.3" />
      |          <!--[if gt IE 6]>
      |            <link rel="stylesheet" media="all" href="/css/ie7.css?version=2017.04.04.3" />
      |          <![endif]-->
      |          <link rel="stylesheet" type="text/css" media="screen" href="/css/custom/add2home.css?version=2017.04.04.3" />
      |          <link rel="stylesheet" type="text/css" media="screen" href="/css/custom/msgBoxLight.css?version=2017.04.04.3" />
      |          <link rel="stylesheet" type="text/css" media="screen" href="/css/custom/jquery-ui-1.9.2.custom.css?version=2017.04.04.3" />
      |          <!--
      |          <link rel="apple-touch-icon-precomposed" href="/apple-touch-icon-precomposed.png">
      |          <link rel="apple-touch-icon-precomposed" sizes="72x72" href="/apple-touch-icon-72x72-precomposed.png">
      |          <link rel="apple-touch-icon-precomposed" sizes="114x114" href="/apple-touch-icon-114x114-precomposed.png">
      |          <link rel="apple-touch-icon-precomposed" sizes="144x144" href="/apple-touch-icon-144x144-precomposed.png">
      |
      |          <link rel="apple-touch-startup-image" href="/apple-touch-startup-image-320x460.png" media="(device-width: 320px) and (device-height: 480px) and (-webkit-device-pixel-ratio: 1)" />
      |          <link rel="apple-touch-startup-image" href="/apple-touch-startup-image-640x920.png" media="(device-width: 320px) and (device-height: 480px) and (-webkit-device-pixel-ratio: 2)" />
      |          <link rel="apple-touch-startup-image" href="/apple-touch-startup-image-640x1096.png" media="(device-width: 320px) and (device-height: 568px) and (-webkit-device-pixel-ratio: 2)" />
      |          <link rel="apple-touch-startup-image" href="/apple-touch-startup-image-768x1004.png" media="(device-width: 768px) and (device-height: 1024px) and (orientation: portrait) and (-webkit-device-pixel-ratio: 1)" />
      |          <link rel="apple-touch-startup-image" href="/apple-touch-startup-image-748x1024.png" media="(device-width: 768px) and (device-height: 1024px) and (orientation: landscape) and (-webkit-device-pixel-ratio: 1)" />
      |          <link rel="apple-touch-startup-image" href="/apple-touch-startup-image-1536x2008.png" media="(device-width: 768px) and (device-height: 1024px) and (orientation: portrait) and (-webkit-device-pixel-ratio: 2)" />
      |          <link rel="apple-touch-startup-image" href="/apple-touch-startup-image-1496x2048.png" media="(device-width: 768px) and (device-height: 1024px) and (orientation: landscape) and (-webkit-device-pixel-ratio: 2)" />
      |          -->
      |          <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico?version=2017.04.04.3">
      |            <!--[if lt IE 9]>
      |              <script type="text/javascript" src="/javascripts/html5shiv.js?version=2017.04.04.3"></script>
      |            <![endif]-->
      |            <script type="text/javascript" src="/javascripts/jquery-1.10.2.js?version=2017.04.04.3"></script>
      |            <script type="text/javascript" src="/javascripts/jquery.fancybox.js?version=2017.04.04.3"></script>
      |            <script type="text/javascript" src="/javascripts/jquery.ketchup.js?version=2017.04.04.3"></script>
      |            <script type="text/javascript" src="/javascripts/jquery-ui-1.10.3.custom.js?version=2017.04.04.3"></script>
      |            <script type="text/javascript" src="/javascripts/jquery.iframe-transport.js?version=2017.04.04.3"></script>
      |            <script type="text/javascript" src="/javascripts/jquery.select2.js?version=2017.04.04.3"></script>
      |            <script type="text/javascript" src="/javascripts/common.js?version=2017.04.04.3"></script>
      |            <script type="text/javascript" src="/javascripts/common_rb.js?version=2017.04.04.3"></script>
      |            <script src="//use.typekit.net/fmk8ztf.js"></script>
      |            <script>try{Typekit.load();}catch(e){}</script>
      |            <!--[if lte IE 8 ]>
      |              <script type="text/javascript" src="/javascripts/ie8.js?version=2017.04.04.3"></script>
      |            <![endif]-->
      |            <script type="text/javascript" src="/javascripts/custom/jquery.validate.js?version=2017.04.04.3"></script>
      |            <script type="text/javascript" src="/javascripts/custom/jquery.validate-additional-methods.js?version=2017.04.04.3"></script>
      |            <script type="text/javascript" src="/javascripts/custom/jquery.ui.datepicker.validation.js?version=2017.04.04.3"></script>
      |            <script type="text/javascript" src="/javascripts/custom/jquery.msgBox.js?version=2017.04.04.3"></script>
      |            <script type="text/javascript" src='/javascripts/custom/jquery.TableCSVExport.js?version=2017.04.04.3'></script>
      |            <script type="text/javascript" src="/javascripts/custom/rankings.js?version=2017.04.04.3"></script>
      |            <script type="text/javascript" src="/javascripts/custom/ios.js?version=2017.04.04.3"></script>
      |            <script>(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
      |  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
      |  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
      |  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
      |
      |  ga('create', 'UA-38391804-1', 'auto');
      |  ga('set', 'anonymizeIp', true);
      |  ga('set', 'forceSSL', true);
      |  ga('send', 'pageview');</script>
      |            <script>var MTIProjectId='9d5408bc-7ac4-4ff0-a210-0fe37a77e240';
      |  (function(){
      |    var m=document.createElement('script');
      |        m.type='text/javascript';
      |        m.async='true';
      |        m.src=('https:'==document.location.protocol?'https:':'http:')+'//fast.fonts.com/t/trackingCode.js';
      |    (document.getElementsByTagName('head')[0]||document.getElementsByTagName('body')[0]).appendChild(m);
      |  })();</script>
      |            <style type="text/css"></style>
      |          </head>
      |          <body id="channel_members" class="british_swimming_rankings">
      |            <div class="cookie-bar">
      |              <div class="container_12 cookie-bar-inner">
      |                <p class="cookie-bar-copy">
      |                  <strong>Our website uses cookies</strong>to improve your browsing experience. If you’d like to learn more about the cookies we set or how to manage what cookies your browser accepts, you can find out on our
      |                  <a href="http://www.swimming.org/information/cookies">cookie information page</a>.
      |                </p>
      |              </div>
      |            </div>
      |            <div id="tabBar">
      |              <div class="container_12">
      |                <a href="/">
      |                  <img src="/images/global/swimmingresults.org-logo.png" id="superLogo" alt="swimmingresults.org"></a>
      |                  <ul id="usefulLinks">
      |                    <li>
      |                      <a href="http://www.britishswimming.org">British Swimming</a>
      |                    </li>
      |                    <li>
      |                      <a href="http://www.swimming.org/swimengland/">Swim England</a>
      |                    </li>
      |                    <li>
      |                      <a href="http://www.swimwales.org/">Swim Wales</a>
      |                    </li>
      |                    <li>
      |                      <a href="http://www.scottishswimming.com/">Scottish Swimming</a>
      |                    </li>
      |                    <li>
      |                      <script type="text/javascript" language="javascript">
      |                        <!--
      |                        // Email obfuscator script 2.1 by Tim Williams, University of Arizona
      |                        // Random encryption key feature by Andrew Moulden, Site Engineering Ltd
      |                        // PHP version coded by Ross Killen, Celtic Productions Ltd
      |                        // This code is freeware provided these six comment lines remain intact
      |                        // A wizard to generate this code is at http://www.jottings.com/obfuscator/
      |                        // The PHP code may be obtained from http://www.celticproductions.net/
      |
      |
      |                        coded = "i7jl97HCsH3TYYTVNv7HhjdHF5vN"
      |                          key = "@avXY4.p1IVU7b2NkL9Si-GM53WtsuPqJAFlzERrQmDOC8fnTeBxgK6dHhZ0cowjy"
      |                          shift=coded.length
      |                          link=""
      |                          for (i=0; i<coded.length; i++) {
      |                            if (key.indexOf(coded.charAt(i))==-1) {
      |                              ltr = coded.charAt(i)
      |                              link += (ltr)
      |                            }
      |                            else {
      |                              ltr = (key.indexOf(coded.charAt(i))-
      |                        shift+key.length) % key.length
      |                              link += (key.charAt(ltr))
      |                            }
      |                          }
      |                        document.write("<a href='mailto:"+link+"'>Contact</a>")
      |
      |                        //-->
      |                      </script>
      |                      <noscript>N/A</noscript>
      |                    </li>
      |                  </ul>
      |                </div>
      |                <div class="container_12">
      |                  <ul id="superTabs" style="height: 0px;"></ul>
      |                </div>
      |              </div>
      |              <div class="container">
      |                <nav class="minor-nav top">
      |                  <ul class="minor-nav-ul">
      |                    <li class="minor-nav-li">
      |                      <a href="/">Home</a>
      |                    </li>
      |                    <li class="minor-nav-li">
      |                      <a class="noclickmenu" href="#">Rankings</a>
      |                      <ul class="sub-nav">
      |                        <li>
      |                          <a href="/12months/">Event Rankings (12 Months)</a>
      |                        </li>
      |                        <li>
      |                          <a href="/eventrankings/">Event Rankings (All Time)</a>
      |                        </li>
      |                        <li>
      |                          <a href="/individualbest/">Individual Best Times</a>
      |                        </li>
      |                        <li>
      |                          <a href="/individualrankings/">Individual Rankings</a>
      |                        </li>
      |                        <li>
      |                          <a href="/progressive/">Progressive Rankings</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.swimrankings.net/index.php?page=rankingDetail&club=EU" target="_blank">European Rankings</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.fina.org/content/swimming-world-ranking" target="_blank">FINA World Rankings</a>
      |                        </li>
      |                      </ul>
      |                    </li>
      |                    <li class="minor-nav-li">
      |                      <a class="noclickmenu" href="#">Results</a>
      |                      <ul class="sub-nav">
      |                        <li>
      |                          <a href="/showmeetsbyclub/">Meet Results By Club</a>
      |                        </li>
      |                        <li>
      |                          <a href="/showmeetsbyevent/">Meet Results By Event</a>
      |                        </li>
      |                        <li>
      |                          <a href="/showmeets/">Meet Results By Swimmer</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.britishswimming.org/rankings-records-results/results/swimming-results/" target="_blank">British Swimming Results</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.swimming.org/sport/major-events/" target="_blank">Swim England Results</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.swimwales.org/events" target="_blank">Swim Wales Results</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.scottishswimming.com/compete/swimming/results.aspx" target="_blank">Scottish Swimming Results</a>
      |                        </li>
      |                      </ul>
      |                    </li>
      |                    <li class="minor-nav-li">
      |                      <a class="noclickmenu" href="#">Records</a>
      |                      <ul class="sub-nav">
      |                        <li>
      |                          <a href="http://www.britishswimming.org/rankings-records-results/records/" target="_blank">British Swimming Records</a>
      |                        </li>
      |                        <li>
      |                          <a href="/records/english.php">Swim England Records</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.swimwales.org/swimming/swimming-records" target="_blank">Swim Wales Records</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.scottishswimming.com/compete/swimming/records.aspx" target="_blank">Scottish Swimming Records</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://records.swimmingresults.org" target="_blank">Regional & County Records Officer Aide-memoire</a>
      |                        </li>
      |                      </ul>
      |                    </li>
      |                    <li class="minor-nav-li">
      |                      <a class="noclickmenu" href="#">Para-Swimming</a>
      |                      <ul class="sub-nav">
      |                        <li>
      |                          <a href="/disabilityrankings/">Event Rankings</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.britishswimming.org/rankings-records-results/records/" target="_blank">British Para-Swimming Records</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.swimming.org/sport/english-swimming-records/" target="_blank">Swim England Para-Swimming Records</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.paralympic.org/Swimming/Results/Rankings" target="_blank">World Para Swimming Rankings</a>
      |                        </li>
      |                      </ul>
      |                    </li>
      |                    <li class="minor-nav-li">
      |                      <a class="noclickmenu" href="#">Masters</a>
      |                      <ul class="sub-nav">
      |                        <li>
      |                          <a href="http://www.swimming.org/masters/" target="_blank">Masters Swimming Hub</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.swimming.org/asa/calendar/filter/masters/" target="_blank">Masters Calendar</a>
      |                        </li>
      |                        <li>
      |                          <a href="/mastersdata/decathlon/">Decathlon Competition</a>
      |                        </li>
      |                        <li>
      |                          <a href="/masterseventrankings/">Event Rankings</a>
      |                        </li>
      |                        <li>
      |                          <a href="/mastersindividualbest/">Individual Best Times</a>
      |                        </li>
      |                        <li>
      |                          <a href="/mastersindividualrankings/">Individual Rankings</a>
      |                        </li>
      |                        <li>
      |                          <a href="/mastersdata/results/">Results</a>
      |                        </li>
      |                        <li>
      |                          <a href="/mastersdata/results/openwater.php">Results - Open Water</a>
      |                        </li>
      |                        <li>
      |                          <a href="/mastersdata/records/">British Records</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.len.eu/?p=4577" target="_blank">European Records</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.fina.org/content/masters-records" target="_blank">FINA World Records</a>
      |                        </li>
      |                      </ul>
      |                    </li>
      |                    <li class="minor-nav-li">
      |                      <a class="active noclickmenu" href="#">Members</a>
      |                      <ul class="sub-nav">
      |                        <li>
      |                          <a href="/clubofficers/">English/Welsh Club Officers List</a>
      |                        </li>
      |                        <li>
      |                          <a href="/member_options/">English/Welsh Member Options</a>
      |                        </li>
      |                        <li>
      |                          <a href="/membershipcheck/">English/Welsh Membership Check</a>
      |                        </li>
      |                        <li>
      |                          <a href="https://www.swimmingmembers.org/" target="_blank">English/Welsh Online Membership</a>
      |                        </li>
      |                        <li>
      |                          <a href="https://www.scottishswimming.com/membership/membership-check.aspx" target="_blank">Scottish Membership Check</a>
      |                        </li>
      |                        <li>
      |                          <a href="https://scottishswimming.azolve.com/Account.mvc/Login?ReturnUrl=%2f" target="_blank">Scottish Online Membership</a>
      |                        </li>
      |                      </ul>
      |                    </li>
      |                    <li class="minor-nav-li">
      |                      <a href="/biogs/">Biogs</a>
      |                    </li>
      |                    <li class="minor-nav-li">
      |                      <a href="#">Entry Tools</a>
      |                      <ul class="sub-nav">
      |                        <li>
      |                          <a href="/competitionentrycheck/">Entry Check (Hy-Tek)</a>
      |                        </li>
      |                        <li>
      |                          <a href="/competitionentrycheck/">Entry Check (SPORTSYS)</a>
      |                        </li>
      |                        <li>
      |                          <a href="/competitiontimescheck/">Entry Time Check (Hy-Tek)</a>
      |                        </li>
      |                        <li>
      |                          <a href="/mrfresultscheck/">MRF Results Data Validation</a>
      |                        </li>
      |                        <li>
      |                          <a href="/teammanagercheck/">Hy-Tek Team Manager Data Validation</a>
      |                        </li>
      |                        <li>
      |                          <a href="/personalkey/">Personal Key</a>
      |                        </li>
      |                        <li>
      |                          <a href="/qt/">Qualifying Times Generator</a>
      |                        </li>
      |                      </ul>
      |                    </li>
      |                    <li class="minor-nav-li">
      |                      <a class="noclickmenu" href="#">Downloads</a>
      |                      <ul class="sub-nav">
      |                        <li>
      |                          <a href="http://www.swimming.org/library/46" target="_blank">Swim England Download Library</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.swimming.org/assets/uploads/events/BS_CP_Handbook.pdf">Competition Pathway Handbook</a>
      |                        </li>
      |                        <li>
      |                          <a href="/clubcodes/GBDualRec.php">Dual Recognition Codes</a>
      |                        </li>
      |                        <li>
      |                          <a href="/downloads/equivalent-time/">Equivalent Time Converter</a>
      |                        </li>
      |                        <li>
      |                          <a href="/downloads/equivalent-time-share/">Equivalent Time Algorithm/DLL</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.fina.org/content/fina-points" target="_blank">FINA Points Calculator</a>
      |                        </li>
      |                        <li>
      |                          <a href="/downloads/Lenex3.0.pdf" target="_blank">Lenex 3.0 Specification</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.swimming.org/library/documents/1214/download" target="_blank">International Permit Form</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.swimming.org/library/documents/1349/download" target="_blank">Neutral Data Format</a>
      |                        </li>
      |                        <li>
      |                          <a href="/clubcodes/">Official Club Codes</a>
      |                        </li>
      |                        <li>
      |                          <a href="/downloads/para-points/">Para-Swimming Points Calculator</a>
      |                        </li>
      |                        <li>
      |                          <a href="/downloads/DWSREFT18.csv">Para-Swimming Reference Times</a>
      |                        </li>
      |                        <li>
      |                          <a href="http://www.swimming.org/library/documents/544/download" target="_blank">Rankings Policy</a>
      |                        </li>
      |                      </ul>
      |                    </li>
      |                    <li class="minor-nav-li">
      |                      <a href="/licensed_meets/">Licensed Meets</a>
      |                    </li>
      |                  </ul>
      |                </nav>
      |                <div id="outerWrapper">
      |                  <div class="global-header nosidenav">
      |                    <h2>Members</h2>
      |                  </div>
      |                  <div class="grid_12 content-wpr no-sidebar">
      |                    <div class="boxTop"></div>
      |                    <div class="boxRepeat rankingsContent">
      |                      <h1>English and Welsh Membership Check</h1>
      |                      <p class="rnk_jr">If the information below is not what you expect please go to the
      |                        <a href="https://www.swimmingmembers.org/" target="_blank">Online Membership System</a>and update your Data Protection Choices.
      |                      </p>
      |                      <div style="display: table; width:100%;">
      |                        <div style="display: table-row;">
      |                          <div style="display: table-cell; vertical-align:top; width:100%">
      |                            <table width="100%" align="center">
      |                              <tr>
      |                                <td width="100%" colspan="4" style="background-color:#ffffbb; padding:4px; text-align:center">
      |                                  <b>Personal Details</b>
      |                                </td>
      |                              </tr>
      |                              <tr>
      |                                <td width="20%" style="background-color:#ECECEC; padding:4px">
      |                                  <b>Name</b>
      |                                </td>
      |                                <td width="30%" style="background-color:#ECECEC; padding:4px">James Reddick</td>
      |                                <td width="20%" style="background-color:#ECECEC; padding:4px">
      |                                  <b>Known As</b>
      |                                </td>
      |                                <td width="30%" style="background-color:#ECECEC; padding:4px">James</td>
      |                              </tr>
      |                              <tr>
      |                                <td width="20%" style="background-color:#F8F8F8; padding:4px">
      |                                  <b>Year of Birth</b>
      |                                </td>
      |                                <td width="30%" style="background-color:#F8F8F8; padding:4px">1972</td>
      |                                <td width="20%" style="background-color:#F8F8F8; padding:4px">
      |                                  <b>Gender</b>
      |                                </td>
      |                                <td width="30%" style="background-color:#F8F8F8; padding:4px">Male</td>
      |                              </tr>
      |                            </table>
      |                            <br>
      |                              <table width="100%" align="center">
      |                                <tr>
      |                                  <td width="100%" colspan="4" style="background-color:#ffffbb; padding:4px; text-align:center">
      |                                    <b>Membership Details</b>
      |                                  </td>
      |                                </tr>
      |                                <tr>
      |                                  <td width="20%" style="background-color:#ECECEC; padding:4px">
      |                                    <b>Member Number</b>
      |                                  </td>
      |                                  <td width="30%" style="background-color:#ECECEC; padding:4px">283261</td>
      |                                  <td width="20%" style="background-color:#ECECEC; padding:4px">
      |                                    <b>Member Status</b>
      |                                  </td>
      |                                  <td width="30%" style="background-color:#ECECEC; padding:4px">Current</td>
      |                                </tr>
      |                                <tr>
      |                                  <td width="20%" style="background-color:#F8F8F8; padding:4px">
      |                                    <b>Fee Paying Club</b>
      |                                  </td>
      |                                  <td width="30%" style="background-color:#F8F8F8; padding:4px">Polytechnic S&WP Club</td>
      |                                  <td width="20%" style="background-color:#F8F8F8; padding:4px">
      |                                    <b>Category</b>
      |                                  </td>
      |                                  <td width="30%" style="background-color:#F8F8F8; padding:4px">SE Cat 2</td>
      |                                </tr>
      |                                <tr>
      |                                  <td width="50%" colspan="2" style="background-color:#ECECEC; padding:4px">
      |                                    <b>Country of International Representation</b>
      |                                  </td>
      |                                  <td width="50%" colspan="2" style="background-color:#ECECEC; padding:4px">England</td>
      |                                </tr>
      |                                <tr>
      |                                  <td style="background-color:#F8F8F8; padding:4px">
      |                                    <b>Discipline</b>
      |                                  </td>
      |                                  <td style="background-color:#F8F8F8; padding:4px; text-align:left">Water Polo Player</td>
      |                                  <td colspan="2" style="background-color:#F8F8F8; padding:4px">&nbsp;</td>
      |                                </tr>
      |                              </table>
      |                              <br>
      |                                <table width="100%">
      |                                  <tr>
      |                                    <td width="100%" colspan="5" style="background-color:#ffffbb; padding:4px; text-align:center">
      |                                      <b>Clubs & Organisations</b>
      |                                    </td>
      |                                  </tr>
      |                                  <tr>
      |                                    <td width="20%" style="background-color:#ECECEC; padding:4px; text-align:center">
      |                                      <b>Relationship</b>
      |                                    </td>
      |                                    <td width="25%" style="background-color:#ECECEC; padding:4px; text-align:center">
      |                                      <b>Club</b>
      |                                    </td>
      |                                    <td width="8%" style="background-color:#ECECEC; padding:4px; text-align:center">
      |                                      <b>Ranked</b>
      |                                    </td>
      |                                    <td width="35%" style="background-color:#ECECEC; padding:4px; text-align:center">
      |                                      <b>Roles</b>
      |                                    </td>
      |                                    <td width="12%" style="background-color:#ECECEC; padding:4px; text-align:center">
      |                                      <b>From</b>
      |                                    </td>
      |                                  </tr>
      |                                  <tr>
      |                                    <td style="background-color:#F8F8F8; padding:4px; text-align:left;">
      |                                      <b>Club Officer</b>
      |                                    </td>
      |                                    <td style="background-color:#F8F8F8; padding:4px; text-align:left;">Polytechnic S&WP Club</td>
      |                                    <td style="background-color:#F8F8F8; padding:4px; text-align:center; ">&nbsp;</td>
      |                                    <td style="background-color:#F8F8F8; padding:4px; text-align:left;">Club Secretary</td>
      |                                    <td style="background-color:#F8F8F8; text-align:center; padding:4px;">03-04-05</td>
      |                                  </tr>
      |                                  <tr>
      |                                    <td style="background-color:#ECECEC; padding:4px; text-align:left;">
      |                                      <b>Club Officer</b>
      |                                    </td>
      |                                    <td style="background-color:#ECECEC; padding:4px; text-align:left;">Tower Hamlets SC</td>
      |                                    <td style="background-color:#ECECEC; padding:4px; text-align:center; ">&nbsp;</td>
      |                                    <td style="background-color:#ECECEC; padding:4px; text-align:left;">Club Secretary</td>
      |                                    <td style="background-color:#ECECEC; text-align:center; padding:4px;">31-01-11</td>
      |                                  </tr>
      |                                  <tr>
      |                                    <td style="background-color:#F8F8F8; padding:4px; text-align:left;">
      |                                      <b>Club Member</b>
      |                                    </td>
      |                                    <td style="background-color:#F8F8F8; padding:4px; text-align:left;">Hackney Aquatics Club</td>
      |                                    <td style="background-color:#F8F8F8; padding:4px; text-align:center; ">&nbsp;</td>
      |                                    <td style="background-color:#F8F8F8; padding:4px; text-align:left;">&nbsp;</td>
      |                                    <td style="background-color:#F8F8F8; text-align:center; padding:4px;">19-04-13</td>
      |                                  </tr>
      |                                </table>
      |                              </div>
      |                            </div>
      |                            <br>
      |                              <div align="center" style="padding-bottom:5px">
      |                                <table width="100%" border="0">
      |                                  <tr>
      |                                    <td width="25%" style="background-color:transparent">
      |                                      <div align="center">
      |                                        <form method="POST" action="/member_options/index.php">
      |                                          <div id="submit_div" style="display: block;">
      |                                            <input type="image" src="/images/custom/form_buttons/member_options.png" name="Submit"></div>
      |                                            <input type="hidden" name="ref" value="283261"></form>
      |                                          </div>
      |                                        </td>
      |                                        <td width="25%" style="background-color:transparent">
      |                                          <div align="center">
      |                                            <a href="./member_details.php?print=1&myiref=283261" target="_blank" onclick="javascript:void window.open('./member_details.php?print=1&myiref=283261','1362058522742','width=1024,height=500,toolbar=0,menubar=0,location=0,status=1,scrollbars=1,resizable=1,left=0,top=0');return false;">
      |                                              <img src="/images/custom/form_buttons/print.png" width="112" height="22" />
      |                                            </a>
      |                                          </div>
      |                                        </td>
      |                                        <td width="25%" style="background-color:transparent">
      |                                          <div align="center">
      |                                            <a href="./member_details.php?print=2&myiref=283261" target="_blank" onclick="javascript:void window.open('./member_details.php?print=2&myiref=283261','1362058522742','width=1024,height=500,toolbar=0,menubar=0,location=0,status=1,scrollbars=1,resizable=1,left=0,top=0');return false;">
      |                                              <img src="/images/custom/form_buttons/pdf.png" width="112" height="22" />
      |                                            </a>
      |                                          </div>
      |                                        </td>
      |                                      </tr>
      |                                    </table>
      |                                  </div>
      |                                </div>
      |                                <p class="rnk_j">Disclosure of your information. Please click
      |                                  <a href="http://www.swimming.org/library/documents/2479/download" target="_blank">here</a>for the Swim England Privacy Policy,
      |                                  <a href="https://www.britishswimming.org/about-us/policy-documents/british-swimming-privacy-policy/" target="_blank">here</a>for the British Swimming Privacy Policy and
      |                                  <a href="https://www.scottishswimming.com/membership/data-protection.aspx" target="_blank">here</a>for the Scottish Swimming Privacy Policy.
      |If you are a member of a Swim England or Swim Wales club and have a membership query please contact
      |                                  <script type="text/javascript" language="javascript">
      |                                    <!--
      |                                    // Email obfuscator script 2.1 by Tim Williams, University of Arizona
      |                                    // Random encryption key feature by Andrew Moulden, Site Engineering Ltd
      |                                    // PHP version coded by Ross Killen, Celtic Productions Ltd
      |                                    // This code is freeware provided these six comment lines remain intact
      |                                    // A wizard to generate this code is at http://www.jottings.com/obfuscator/
      |                                    // The PHP code may be obtained from http://www.celticproductions.net/
      |
      |
      |                                    coded = "TjFjLoDnOnLyllyFa2UTa"
      |                                      key = ".T5OCL3JZ9IynWRoDqxA-2YHufhb07MvQFBXU6gm1kde4rG@Vw8SpPcisztalENKj"
      |                                      shift=coded.length
      |                                      link=""
      |                                      for (i=0; i<coded.length; i++) {
      |                                        if (key.indexOf(coded.charAt(i))==-1) {
      |                                          ltr = coded.charAt(i)
      |                                          link += (ltr)
      |                                        }
      |                                        else {
      |                                          ltr = (key.indexOf(coded.charAt(i))-
      |                                    shift+key.length) % key.length
      |                                          link += (key.charAt(ltr))
      |                                        }
      |                                      }
      |                                    document.write("<a href='mailto:"+link+"'>renewals@swimming.org</a>")
      |
      |                                    //-->
      |                                  </script>
      |                                  <noscript>N/A</noscript>.
      |To update your contact details, website visibility or data protection choices log on to the
      |                                  <a href="https://www.swimmingmembers.org/" target="_blank">Swim England & Swim Wales Online Membership System</a>.
      |Scottish members need to use the
      |                                  <a href="https://scottishswimming.azolve.com/Account.mvc/LogIn">Scottish Membership System</a>.
      |If you believe times are wrong or missing from a meet then please contact the MEET PROMOTER with the details as we are unable to help with these queries.
      |Questions concerning Masters results or the Decathlon competition should be sent
      |                                  <script type="text/javascript" language="javascript">
      |                                    <!--
      |                                    // Email obfuscator script 2.1 by Tim Williams, University of Arizona
      |                                    // Random encryption key feature by Andrew Moulden, Site Engineering Ltd
      |                                    // PHP version coded by Ross Killen, Celtic Productions Ltd
      |                                    // This code is freeware provided these six comment lines remain intact
      |                                    // A wizard to generate this code is at http://www.jottings.com/obfuscator/
      |                                    // The PHP code may be obtained from http://www.celticproductions.net/
      |
      |
      |                                    coded = "WRzKW4QRHrfGuZn.rHmnY@Ww4n7"
      |                                      key = "VWRy8HXYmN-qJThGoMe.uK4kztlExsfCA2376LSdaQFnjrwI1DOgZ@BP0pbic5vU9"
      |                                      shift=coded.length
      |                                      link=""
      |                                      for (i=0; i<coded.length; i++) {
      |                                        if (key.indexOf(coded.charAt(i))==-1) {
      |                                          ltr = coded.charAt(i)
      |                                          link += (ltr)
      |                                        }
      |                                        else {
      |                                          ltr = (key.indexOf(coded.charAt(i))-
      |                                    shift+key.length) % key.length
      |                                          link += (key.charAt(ltr))
      |                                        }
      |                                      }
      |                                    document.write("<a href='mailto:"+link+"'>here</a>")
      |
      |                                    //-->
      |                                  </script>
      |                                  <noscript>N/A</noscript>.
      |                                  <!--       Only technical problems with this website should be sent <script type="text/javascript" language="javascript">
      |                                  <!--
      |                                  // Email obfuscator script 2.1 by Tim Williams, University of Arizona
      |                                  // Random encryption key feature by Andrew Moulden, Site Engineering Ltd
      |                                  // PHP version coded by Ross Killen, Celtic Productions Ltd
      |                                  // This code is freeware provided these six comment lines remain intact
      |                                  // A wizard to generate this code is at http://www.jottings.com/obfuscator/
      |                                  // The PHP code may be obtained from http://www.celticproductions.net/
      |
      |
      |                                  coded = "HmBcMm93A9lRaaRDbfm9WBd9IOfb"
      |                                    key = "OYgi35y@aUlGfWMp4en7hNjts0SFE1bRJ.2AxoBP-kVc8mDwHrud9LQqCvzKXIZ6T"
      |                                    shift=coded.length
      |                                    link=""
      |                                    for (i=0; i<coded.length; i++) {
      |                                      if (key.indexOf(coded.charAt(i))==-1) {
      |                                        ltr = coded.charAt(i)
      |                                        link += (ltr)
      |                                      }
      |                                      else {
      |                                        ltr = (key.indexOf(coded.charAt(i))-
      |                                  shift+key.length) % key.length
      |                                        link += (key.charAt(ltr))
      |                                      }
      |                                    }
      |                                  document.write("<a href='mailto:"+link+"'>here</a>")
      |
      |                                  //-->
      |                                </script>
      |                                <noscript>N/A</noscript>
      |                              </p>
      |                            </div>
      |                            <div class="boxBottom"></div>
      |                            <div class="boxBottom"></div>
      |                          </div>
      |                        </div>
      |                        <div class="footer-wpr">
      |                          <footer>
      |                            <img class="recognition-bar" src="/images/recog/swimmingresults-transparent.png" alt="Sponsors"></footer>
      |                            <div class="colophon-wpr">
      |                              <div class="colophon">
      |                                <p>© Copyright 2018</p>
      |                                <p style="float: right;">Powered By.
      |                                  <a href="http://www.sportsys.co.uk/" target="_blank">SPORTSYS LLP</a>
      |                                </p>
      |                                <a href="//www.swimmingresults.org/project_honey_pot/rewarding.php">
      |                                  <span style="display: none;">fact-groovy</span>
      |                                </a>
      |                              </div>
      |                            </div>
      |                          </div>
      |                        </div>
      |                      </body>
      |                    </html>
    """.stripMargin
}
