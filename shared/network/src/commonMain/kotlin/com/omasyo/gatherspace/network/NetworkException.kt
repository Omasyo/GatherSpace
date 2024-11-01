package com.omasyo.gatherspace.network

import com.omasyo.gatherspace.models.response.ErrorResponse

class NetworkException(val error: ErrorResponse) : Exception(error.message)
