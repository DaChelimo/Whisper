package com.da_chelimo.whisper.network_moniter

//class NetworkMonitor(
//    private val userDetailsRepo: UserDetailsRepo = UserDetailsRepoImpl()
//) {
//
//    fun setupCallback(context: Context, isApplicationOpened: Boolean) {
//        val connectivityManager =
//            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
//
//        connectivityManager.registerDefaultNetworkCallback(
//            object : ConnectivityManager.NetworkCallback() {
//
//            override fun onAvailable(network: Network) {
//                super.onAvailable(network)
//                Timber.d("Service: onAvailable called")
//
//                Firebase.auth.uid?.let { uid ->
//                    userDetailsRepo.updateUserLastSeen(
//                        uid,
//                        if (isApplicationOpened) UserStatus.Active else UserStatus.HasDataButNotInApp
//                    )
//                }
//            }
//
//            override fun onLosing(network: Network, maxMsToLive: Int) {
//                super.onLosing(network, maxMsToLive)
//                Timber.d("Service: onLosing called with maxMsToLive as $maxMsToLive")
//
//                Firebase.auth.uid?.let { uid ->
//                    userDetailsRepo.updateUserLastSeen(
//                        uid,
//                        UserStatus.Offline
//                    )
//                }
//            }
//
//                override fun onLost(network: Network) {
//                    super.onLost(network)
//                    Timber.d("Service: onLost called")
//
//                    Firebase.auth.uid?.let { uid ->
//                        userDetailsRepo.updateUserLastSeen(
//                            uid,
//                            UserStatus.Offline
//                        )
//                    }
//                }
//        })
//    }
//
//}