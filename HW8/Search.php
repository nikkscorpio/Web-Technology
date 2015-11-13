

<?php
global $totalEntries;

error_reporting(E_ALL);

$endpoint = 'http://svcs.ebay.com/services/search/FindingService/v1'; 
$version = '1.0.0'; 
$appid = 'NikithaR-6a07-4088-84ce-94b35285b8d5';  
$siteid = '0';
$query = trim($_GET["Keywords"]); 
$safequery = urlencode($query);  
$i = '0';
 $x = 0;
 $a = 0;


// Construct the findItemsByKeywords HTTP GET call
$apicall = "$endpoint?";
$apicall .= "OPERATION-NAME=findItemsAdvanced";
$apicall .= "&SERVICE-VERSION=$version";
$apicall .= "&SECURITY-APPNAME=$appid";
$apicall .= "&RESPONSE-DATA-FORMAT=XML";
$apicall .= "&siteid=0";
$apicall .= "&keywords=$safequery";
$apicall .= "&sortOrder=$_GET[sortOrder]";
$apicall .= "&paginationInput.entriesPerPage=$_GET[EntriesPerPage]";
$apicall .= "&outputSelector[1]=SellerInfo&outputSelector[2]=PictureURLSuperSize&outputSelector[3]=StoreInfo";
$apicall .= "&paginationInput.pageNumber=$_GET[pageNumber]";



if(isset($_GET["FixedPrice"]) || isset($_GET["Auction"]) || isset($_GET["Classified"])){
$apicall .= "&itemFilter[$a].name=ListingType";
if(isset($_GET["FixedPrice"])){
$apicall .= "&itemFilter[$a].value[$x]=$_GET[FixedPrice]";
$x++;}
if(isset($_GET["Auction"])){
$apicall .= "&itemFilter[$a].value[$x]=$_GET[Auction]";
$x++;}
if(isset($_GET["Classified"])){
$apicall .= "&itemFilter[$a].value[$x]=$_GET[Classified]";
$x++;}
$a++;
}
if(isset($_GET["NewItem"]) || isset($_GET["Used"]) || isset($_GET["VeryGood"]) || isset($_GET["Good"]) || isset($_GET["Acceptable"])){
$x=0;
$apicall .= "&itemFilter[$a].name=Condition";
if(isset($_GET["NewItem"])){
$apicall .= "&itemFilter[$a].value[$x]=$_GET[NewItem]";
$x++;}
if(isset($_GET["Used"])){
$apicall .= "&itemFilter[$a].value[$x]=$_GET[Used]";
$x++;}
if(isset($_GET["VeryGood"])){
$apicall .= "&itemFilter[$a].value[$x]=$_GET[VeryGood]";
$x++;}
if(isset($_GET["Good"])){
$apicall .= "&itemFilter[$a].value[$x]=$_GET[Good]";
$x++;}
if(isset($_GET["Acceptable"])){
$apicall .= "&itemFilter[$a].value[$x]=$_GET[Acceptable]";
$x++;}
$a++;
}
if(isset($_GET["MinPrice"]) && $_GET["MinPrice"]!=''){
$apicall .= "&itemFilter[$a].name=MinPrice&itemFilter[$a].value=$_GET[MinPrice]";
$a++;}
if(isset($_GET["MaxPrice"]) && $_GET["MaxPrice"]!=''){
$apicall .= "&itemFilter[$a].name=MaxPrice&itemFilter[$a].value=$_GET[MaxPrice]";
$a++;}
if(isset($_GET["FreeShippingOnly"])){
$apicall .= "&itemFilter[$a].name=FreeShippingOnly&itemFilter[$a].value=true";
$a++;}
if(isset($_GET["ExpeditedShippingType"])){
$apicall .= "&itemFilter[$a].name=ExpeditedShippingType&itemFilter[$a].value=Expedited";
$a++;}
if(isset($_GET["ReturnsAcceptedOnly"])){
$apicall .= "&itemFilter[$a].name=ReturnsAcceptedOnly&itemFilter[$a].value=true";
$a++;}
if(isset($_GET["MaxHandlingTime"]) && trim($_GET["MaxHandlingTime"])!=''){
$apicall .=  "&itemFilter[$a].name=MaxHandlingTime&itemFilter[$a].value=$_GET[MaxHandlingTime]";
$a++;}
// Load the call and capture the document returned by eBay API
$resp = simplexml_load_file($apicall);


	 	
 	

 // Check to see if the request was successful, else print an error
if ($resp->ack == "Success") {
	$totalEntries = $resp->paginationOutput->totalEntries;
	
	if($totalEntries == 0){
	$result = array("ack" => "No results found");	
	}
	else{
	$result = array("ack" => "$resp->ack");
	$result["resultCount"] = "$totalEntries";
	$pageNumber = $resp->paginationOutput->pageNumber;
	$result["pageNumber"] = "$pageNumber";
	$itemCount = $resp->paginationOutput->entriesPerPage;
	$result["itemCount"] = "$itemCount";

  $num = 0;
  foreach($resp->searchResult->item as $item) {
  	
  	$itemnum = 'item'.$num;

  	//Assign all values from the XML response to specific php vars
  	//BASIC INFO ITEMS
  	$title = $item->title;
  	$viewItemURL  = $item->viewItemURL;
  	$galleryURL   = $item->galleryURL;
  	$pictureURLSuperSize = $item->pictureURLSuperSize;
  	$convertedCurrentPrice = $item->sellingStatus->convertedCurrentPrice;
  	$shippingServiceCost = $item->shippingInfo->shippingServiceCost;
  	$conditionDisplayName = ($item->condition->conditionDisplayName == "")?"N/A":$item->condition->conditionDisplayName;
  	$listingType = $item->listingInfo->listingType;
  	$location = $item->location;
  	$categoryName = $item->primaryCategory->categoryName;
  	$topRatedListing =$item->topRatedListing;


  	//Check for various LISTINGTYPE 
	if($listingType == "FixedPrice" || $listingType == "StoreInventory" )
	$listingType = "Buy It Now";
	else if($listingType == "Auction" )
	$listingType = "Auction";
	else if($listingType == "Classified" )
	$listingType = "Classified Ad";
	
	//End of LISTINGTYPE check


  	//SELLER INFO
  	$sellerUserName = $item->sellerInfo->sellerUserName;
  	$feedbackScore = $item->sellerInfo->feedbackScore;
  	$positiveFeedbackPercent = $item->sellerInfo->positiveFeedbackPercent;
  	$feedbackRatingStar = $item->sellerInfo->feedbackRatingStar;
  	$topRatedSeller = $item->sellerInfo->topRatedSeller;
  	$sellerStoreName = $item->storeInfo->storeName;
  	$sellerStoreURL = $item->storeInfo->storeURL;


  	//SHIPPING INFO
  	
  	
  	$expeditedShipping = $item->shippingInfo->expeditedShipping;
  	$oneDayShippingAvailable = $item->shippingInfo->oneDayShippingAvailable;
  	$returnsAccepted = $item->returnsAccepted;
  	$handlingTime = $item->shippingInfo->handlingTime;

	$locations = $item->shippingInfo->shipToLocations;
	$shipToLocations = "";
	for($z=0;$z<count($locations);$z++){
	$shipToLocations = $shipToLocations.($locations[$z].",");
	}
	$shipToLocations = rtrim($shipToLocations, ",");

	$shippingType = $item->shippingInfo->shippingType;
	$shippingType = preg_replace_callback('/(?<!\b)[A-Z][a-z]+|(?<=[a-z])[A-Z]/', function($match) {
												return ' '. $match[0];
											}, $shippingType);




   $result["$itemnum"] = array(
   								"basicinfo"=>array(
   									"title" => "$title",
   									"viewItemURL" => "$viewItemURL",
   									"galleryURL" => "$galleryURL",
   									"pictureURLSuperSize" => "$pictureURLSuperSize",
   									"convertedCurrentPrice" => "$convertedCurrentPrice",
   									"shippingServiceCost" => "$shippingServiceCost",
   									"conditionDisplayName" => "$conditionDisplayName",
   									"listingType" => "$listingType",
   									"location" => "$location",
   									"categoryName" => "$categoryName",
   									"topRatedListing" => "$topRatedListing"

   									),
   								"sellerInfo"=>array(
   									"sellerUserName" => "$sellerUserName",
   									"feedbackScore" => "$feedbackScore",
   									"positiveFeedbackPercent" => "$positiveFeedbackPercent",
   									"feedbackRatingStar" => "$feedbackRatingStar",
   									"topRatedSeller" => "$topRatedSeller",
   									"sellerStoreName" => "$sellerStoreName",
   									"sellerStoreURL" => "$sellerStoreURL"
   									),
   								"shippingInfo"=>array(
   									"shippingType" => "$shippingType",
   									"shipToLocations" => "$shipToLocations",
   									"expeditedShipping" => "$expeditedShipping",
   									"oneDayShippingAvailable" => "$oneDayShippingAvailable",
   									"returnsAccepted" => "$returnsAccepted",
   									"handlingTime" => "$handlingTime"
   									)

   						); 
   $num = $num+1;
}
}


}

// If the response does not indicate 'Success,' print an error
else {
  $result = array("ack" => "Error");
}
echo (json_encode($result));
?>

